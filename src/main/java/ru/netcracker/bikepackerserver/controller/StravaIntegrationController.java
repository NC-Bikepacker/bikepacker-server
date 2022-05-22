package ru.netcracker.bikepackerserver.controller;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.NoFileException;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.TrackImageService;
import ru.netcracker.bikepackerserver.service.TrackServiceImpl;
import ru.netcracker.bikepackerserver.service.UserService;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/strava")
@Api(tags = {"Strava integration controller: reads all tracks and user info from zip archive and then adds it to the user profile"})
public class StravaIntegrationController {


    private UserServiceImpl userService;
    private TrackServiceImpl trackService;

    @Autowired
    public StravaIntegrationController(TrackServiceImpl trackService, UserServiceImpl userService) {
        this.userService = userService;
        this.trackService = trackService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity postDataFromZip(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long userId
    ) {
        Map<String, String> activities = new HashMap<>();
        try {
            UserModel user = userService.readById(userId);
            if (file == null) throw new NoFileException();
            ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(file.getBytes()));
            ZipEntry entry = null;
            while ((entry = zin.getNextEntry()) != null) {
                // GPXES
                if (entry.getName().contains("activities/") && !entry.isDirectory()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream((int) entry.getSize());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        baos.write(c);
                    }
                    byte[] bytes = baos.toByteArray();

                    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder;
                    Document doc;
                    try {

                        docBuilder = docFactory.newDocumentBuilder();
                        doc = docBuilder.parse(new ByteArrayInputStream(bytes));
                        // out
                        DOMSource domSource = new DOMSource(doc);
                        StringWriter writer = new StringWriter();
                        StreamResult result = new StreamResult(writer);
                        TransformerFactory tf = TransformerFactory.newInstance();
                        Transformer transformer = tf.newTransformer();
                        transformer.transform(domSource, result);
                        activities.put(entry.getName(), writer.toString());
                    } catch (ParserConfigurationException | SAXException | TransformerException e) {
                        e.printStackTrace();
                    } finally {
                        baos.close();
                    }
                    // TRACKS
                } else if (entry.getName().equals("activities.csv")) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream((int) entry.getSize());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        baos.write(c);
                    }
                    byte[] bytes = baos.toByteArray();
                    baos.close();

                    CSVParser parser = CSVFormat.newFormat(',').withQuote('"').withHeader().parse(
                            new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8));
                    // out
                    System.out.println(entry.getName() + " in String format is:");
                    for (CSVRecord record : parser) {
                        try {
                            TrackModel trackModel = new TrackModel();
                            trackModel.setTrackDate(
                                    LocalDateTime.parse(
                                            record.get("Activity Date"),
                                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(Locale.US)
                                    ).toLocalDate()
                            );
                            trackModel.setTrackName(record.get("Activity Name"));
                            trackModel.setTravelTime((long) Double.parseDouble(record.get("Elapsed Time")));
                            trackModel.setTrackDistance(Double.parseDouble(record.get("Distance")));
                            String gpxStr = activities.get(record.get("Filename"));
                            trackModel.setGpx(gpxStr);
                            List<WayPoint> points = GPX.read(new ByteArrayInputStream(gpxStr.getBytes(StandardCharsets.UTF_8)))
                                    .getTracks()
                                    .get(0)
                                    .getSegments()
                                    .get(0)
                                    .getPoints();
                            WayPoint start = points.get(0);
                            WayPoint finish = points.get(points.size() - 1);
                            trackModel.setTrackStartLat(start.getLatitude().doubleValue());
                            trackModel.setTrackStartLon(start.getLongitude().doubleValue());
                            trackModel.setTrackFinishLat(finish.getLatitude().doubleValue());
                            trackModel.setTrackFinishLon(finish.getLongitude().doubleValue());
                            trackModel.setTrackAvgSpeed(Double.parseDouble(record.get("Average Speed")));
                            trackModel.setTrackComplexity(Double.parseDouble(record.get("Perceived Exertion")) / 2);
                            UserModel userModel = userService.readById(userId);
                            trackModel.setUser(userModel);
                            System.out.println("\n" + record.get("Activity Date"));
                            System.out.println(record.get("Activity Name"));
//                            System.out.println(record.get("Elapsed Time"));
//                            System.out.println(record.get("Distance"));
//                            System.out.println(gpxStr.substring(0, 1000));
//                            System.out.println(start);
//                            System.out.println(finish);
//                            System.out.println(record.get("Average Speed"));
//                            System.out.println(record.get("Perceived Exertion"));
                            trackService.save(trackModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    parser.close();
                    // USER PROFILE INFO
                } else if (entry.getName().equals("profile.csv")) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream((int) entry.getSize());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        baos.write(c);
                    }
                    byte[] bytes = baos.toByteArray();
                    baos.close();

                    CSVParser parser = CSVFormat.newFormat(',').withQuote('"').withHeader().parse(
                            new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8));
                    // out
                    System.out.println(entry.getName() + " in String format is:");
                    for (CSVRecord record : parser) {
                        try {
                            System.out.println(record.get("First Name"));
                            System.out.println(record.get("Last Name"));
                            user.setFirstname(record.get("First Name"));
                            user.setLastname(record.get("Last Name"));
                            userService.update(user,userId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    parser.close();
                }
            }
            return new ResponseEntity<>(activities.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
