package ru.netcracker.bikepackerserver.controller;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.TrackImageService;
import ru.netcracker.bikepackerserver.service.TrackServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/tracks")
@Validated
@Api(tags = {"Track controller: creating and getting tracks"})
public class TrackController {

    private TrackRepo trackRepo;
    private UserRepo userRepo;
    private TrackImageService trackImageService;
    private TrackServiceImpl trackService;

    @Autowired
    public TrackController(TrackRepo trackRepo, UserRepo userRepo, TrackImageService trackImageService, TrackServiceImpl trackService) {
        this.trackRepo = trackRepo;
        this.userRepo = userRepo;
        this.trackImageService = trackImageService;
        this.trackService = trackService;
    }

//    public static void main(String[] args) {
//        List<TrackModel> tracks = new ArrayList<>();
//        TrackModel tr = new TrackModel();
//        //region n
//        String gpx = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<gpx creator=\"StravaGPX\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\" version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\">\n" +
//                " <trk>\n" +
//                "  <name>Дневной забег</name>\n" +
//                "  <type>9</type>\n" +
//                "  <trkseg>\n" +
//                "   <trkpt lat=\"51.7506800\" lon=\"39.1921360\">\n" +
//                "    <ele>171.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7506810\" lon=\"39.1921370\">\n" +
//                "    <ele>171.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7507930\" lon=\"39.1920800\">\n" +
//                "    <ele>171.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7509180\" lon=\"39.1920710\">\n" +
//                "    <ele>171.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7509680\" lon=\"39.1921020\">\n" +
//                "    <ele>171.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7510100\" lon=\"39.1921440\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7510440\" lon=\"39.1921900\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7510700\" lon=\"39.1922290\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7510830\" lon=\"39.1922500\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7511550\" lon=\"39.1923480\">\n" +
//                "    <ele>171.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7512140\" lon=\"39.1924180\">\n" +
//                "    <ele>171.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7512310\" lon=\"39.1924420\">\n" +
//                "    <ele>171.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7513050\" lon=\"39.1926090\">\n" +
//                "    <ele>172.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7513220\" lon=\"39.1926480\">\n" +
//                "    <ele>172.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7514440\" lon=\"39.1929000\">\n" +
//                "    <ele>171.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7514580\" lon=\"39.1929350\">\n" +
//                "    <ele>171.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7515550\" lon=\"39.1931200\">\n" +
//                "    <ele>171.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7516130\" lon=\"39.1933120\">\n" +
//                "    <ele>171.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7516680\" lon=\"39.1934280\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7517650\" lon=\"39.1936650\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7517820\" lon=\"39.1937030\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7518750\" lon=\"39.1938350\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7519230\" lon=\"39.1939500\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7519410\" lon=\"39.1940290\">\n" +
//                "    <ele>170.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7520240\" lon=\"39.1943050\">\n" +
//                "    <ele>170.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7520400\" lon=\"39.1943370\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7520880\" lon=\"39.1943880\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7521180\" lon=\"39.1944060\">\n" +
//                "    <ele>170.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7522000\" lon=\"39.1945450\">\n" +
//                "    <ele>170.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7522680\" lon=\"39.1946790\">\n" +
//                "    <ele>170.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7523350\" lon=\"39.1948540\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7524580\" lon=\"39.1950700\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7525520\" lon=\"39.1952060\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7526020\" lon=\"39.1952470\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7527450\" lon=\"39.1953100\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7529440\" lon=\"39.1953880\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7529690\" lon=\"39.1954070\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7529990\" lon=\"39.1954350\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7531530\" lon=\"39.1956560\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7532470\" lon=\"39.1958550\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7532770\" lon=\"39.1959610\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7532840\" lon=\"39.1959950\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7533210\" lon=\"39.1961610\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7533260\" lon=\"39.1961910\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7534100\" lon=\"39.1963950\">\n" +
//                "    <ele>170.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7535340\" lon=\"39.1965980\">\n" +
//                "    <ele>170.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7535560\" lon=\"39.1966200\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7536730\" lon=\"39.1967690\">\n" +
//                "    <ele>170.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7537570\" lon=\"39.1968710\">\n" +
//                "    <ele>171.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7538010\" lon=\"39.1969190\">\n" +
//                "    <ele>170.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7538310\" lon=\"39.1969510\">\n" +
//                "    <ele>170.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7539310\" lon=\"39.1970340\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7539640\" lon=\"39.1971010\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7539960\" lon=\"39.1971450\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7540880\" lon=\"39.1972750\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7541100\" lon=\"39.1972960\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7542250\" lon=\"39.1974380\">\n" +
//                "    <ele>170.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7542540\" lon=\"39.1974690\">\n" +
//                "    <ele>170.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7543560\" lon=\"39.1975500\">\n" +
//                "    <ele>169.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7544050\" lon=\"39.1975920\">\n" +
//                "    <ele>169.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7544280\" lon=\"39.1976180\">\n" +
//                "    <ele>169.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7544470\" lon=\"39.1976490\">\n" +
//                "    <ele>169.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7544870\" lon=\"39.1977090\">\n" +
//                "    <ele>169.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7546470\" lon=\"39.1978770\">\n" +
//                "    <ele>169.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7547070\" lon=\"39.1979820\">\n" +
//                "    <ele>169.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7547830\" lon=\"39.1981080\">\n" +
//                "    <ele>168.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7548600\" lon=\"39.1981830\">\n" +
//                "    <ele>168.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7549590\" lon=\"39.1983250\">\n" +
//                "    <ele>168.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7550630\" lon=\"39.1984820\">\n" +
//                "    <ele>168.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7551990\" lon=\"39.1986820\">\n" +
//                "    <ele>168.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7552560\" lon=\"39.1987780\">\n" +
//                "    <ele>169.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7553360\" lon=\"39.1989530\">\n" +
//                "    <ele>169.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7554420\" lon=\"39.1990740\">\n" +
//                "    <ele>168.8</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7554660\" lon=\"39.1990980\">\n" +
//                "    <ele>168.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7556470\" lon=\"39.1992780\">\n" +
//                "    <ele>169.0</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7557810\" lon=\"39.1994620\">\n" +
//                "    <ele>169.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7559310\" lon=\"39.1996610\">\n" +
//                "    <ele>169.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7559650\" lon=\"39.1997270\">\n" +
//                "    <ele>169.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7560700\" lon=\"39.1998770\">\n" +
//                "    <ele>169.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7562360\" lon=\"39.2000030\">\n" +
//                "    <ele>169.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7562900\" lon=\"39.2000500\">\n" +
//                "    <ele>169.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7563770\" lon=\"39.2001630\">\n" +
//                "    <ele>169.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7564190\" lon=\"39.2002350\">\n" +
//                "    <ele>169.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7564380\" lon=\"39.2002570\">\n" +
//                "    <ele>169.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7565990\" lon=\"39.2004260\">\n" +
//                "    <ele>169.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7567600\" lon=\"39.2005740\">\n" +
//                "    <ele>169.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7568950\" lon=\"39.2007060\">\n" +
//                "    <ele>169.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7570330\" lon=\"39.2008360\">\n" +
//                "    <ele>169.6</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7570500\" lon=\"39.2008550\">\n" +
//                "    <ele>169.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7570860\" lon=\"39.2009440\">\n" +
//                "    <ele>169.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7571110\" lon=\"39.2009730\">\n" +
//                "    <ele>169.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7571430\" lon=\"39.2010020\">\n" +
//                "    <ele>169.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7571880\" lon=\"39.2010430\">\n" +
//                "    <ele>169.4</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7572490\" lon=\"39.2011080\">\n" +
//                "    <ele>169.2</ele>\n" +
//                "   </trkpt>\n" +
//                "   <trkpt lat=\"51.7572650\" lon=\"39.2011280\">\n" +
//                "    <ele>169.2</ele>\n" +
//                "</trkpt>\n" +
//                "  </trkseg>\n" +
//                " </trk>\n" +
//                "</gpx>";
//        //endregion
//        tr.setGpx(gpx);
//        tracks.add(tr);
//        List<WayPoint> wayPoints = tracks.stream()
//                .map(o -> {
//                    GPX result = null;
//                    try {
//                        result = GPX.read(new ByteArrayInputStream(o.getGpx().getBytes(StandardCharsets.UTF_8)));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return Objects.requireNonNull(result);
//                })
//                .flatMap(GPX::tracks)
//                .flatMap(Track::segments)
//                .flatMap(
//                        trackSegment ->
//                                Stream.of(
//                                        trackSegment
//                                                .points()
//                                                .findFirst()
//                                                .orElse(
//                                                        WayPoint.of(0, 0)
//                                                )
//                                )
//                )
//                .collect(Collectors.toList());
//        System.out.println(wayPoints);
//    }

    @GetMapping
    @ApiOperation(value = "Get all tracks in the app", notes = "This request returns a list of all of the tracks in DB")
    public List<TrackModel> getTracks() {
        return trackService.getAllTracks();
    }

    @GetMapping("/{id}")
    public ResponseEntity getTrackByUser(@PathVariable(name = "id") Long id) {
        if (id != null) {
            return new ResponseEntity(trackService.getTracksForUser(id), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bydistance/{userid}/{lat}/{lon}")
    public ResponseEntity getTracksSortedByDistance(@PathVariable(name = "userid") Long userId,
                                                    @PathVariable(name = "lat") double lat,
                                                    @PathVariable(name = "lon") double lon) {
        if (userId != null) {
            List<TrackModel> tracks = trackService.getTracksForUser(userId);
            WayPoint location = WayPoint.of(lat, lon);
            Map<Double, TrackModel> tracksMap = new HashMap<>();
            LoggerFactory.getLogger(TrackController.class).debug(tracks.toString());
            for (TrackModel trackModel : tracks) {
                GPX gpx = null;
                try {
                    String gpxStr = trackModel.getGpx();
                    if (gpxStr != null) {
                        gpx = GPX.read(new ByteArrayInputStream(gpxStr.getBytes(StandardCharsets.UTF_8)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (gpx != null) {
                    for (Track track : gpx.getTracks()) {
                        if (track != null) {
                            for (TrackSegment segment : track.getSegments()) {
                                WayPoint start;
                                Optional<WayPoint> optional = segment.getPoints().stream().findFirst();
                                if (optional.isPresent())
                                    start = optional.get();
                                else continue;
                                double length = Geoid.WGS84.distance(start, location).doubleValue();
                                tracksMap.put(length, trackModel);
                            }
                        }
                    }
                }
            }
            List<TrackModel> result = new ArrayList<>();
            List<Double> keys = tracksMap.keySet().stream().sorted(Double::compareTo).collect(Collectors.toList());
            for (Double key : keys) {
                result.add(tracksMap.get(key));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bycomplexity/{userid}")
    public ResponseEntity getTracksSortedByComplexity(@PathVariable(name = "userid") Long userId) {
        if (userId != null) {
            List<TrackModel> tracks = trackService.getTracksForUser(userId);
            List<TrackModel> result = tracks.stream()
                    .sorted(Comparator.comparingDouble(TrackModel::getTrackComplexity))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bytime/{userid}")
    public ResponseEntity getTracksSortedByTime(@PathVariable(name = "userid") Long userId) {
        if (userId != null) {
            List<TrackModel> tracks = trackService.getTracksForUser(userId);
            List<TrackModel> result = tracks.stream()
                    .sorted(Comparator.comparingLong(TrackModel::getTravelTime))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getonetrack/{id}")
    @ApiOperation(value = "get one track", notes = "This request getting one track")
    public ResponseEntity getOneTrack(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "track id",
                    example = "13",
                    required = true
            )
            @PathVariable @Valid Long id
    ) {
        if (id != null) {
            return new ResponseEntity(trackService.getOneTrack(id), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getlastfriendtracks/{id}")
    @ApiOperation(value = "get one track", notes = "This request getting one track")
    public ResponseEntity getLastFriendTracks(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "track id",
                    example = "13",
                    required = true
            )
            @PathVariable @Valid Long id
    ){
        if(id != null){
            return new ResponseEntity(trackService.getLastFriendTracks(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create a new track", notes = "This request creates a new track")
    public ResponseEntity createTrack(
            @RequestBody
            @ApiParam(
                    name = "Track Model",
                    type = "TrackModel",
                    value = "TrackModel",
                    required = true
            )
                    TrackModel track
    ){
    try{
        System.out.println(track.toString());
        return new ResponseEntity(trackService.save(track).getTrackId(), HttpStatus.OK);
    }
    catch (Exception e){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTrack(@PathVariable(name = "id") Long id) {
        try {
            trackService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "Update a track data", notes = "This request changes current track")
    public ResponseEntity updateTrack(
            @ApiParam(
                    name = "id",
                    type = "Long",
                    value = "134",
                    example = "134",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody TrackModel trackModel

    ) {
        trackService.update(trackModel);
        return new ResponseEntity(HttpStatus.OK);
    }
}