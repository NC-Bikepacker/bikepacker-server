package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;



@Service
public class TrackImageService {

    private final ImageRepo imageRepo;
    private final String USER_AGENT = "Mozilla/5.0";

    @Autowired
    public TrackImageService(ImageRepo imageRepo) {
        this.imageRepo = imageRepo;
    }


    public void saveImage(TrackEntity track) throws Exception {
        ImageEntity image = new ImageEntity();
        image.setImageBase64(sendGet(getUrl(track)));
        image.setTrack(track);
        imageRepo.save(image);
    }

    private String sendGet(String url) throws Exception{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Значение по умолчанию - GET
        con.setRequestMethod("GET");
        // Добавляем заголовок запроса
        con.setRequestProperty("User-Agent", USER_AGENT);
        InputStream inputStream=con.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        byte[] bytes;
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }

        bytes = output.toByteArray();
        String encodedString = Base64.getEncoder().encodeToString(bytes);
        inputStream.close();
        return encodedString;
    }

    private String getUrl(TrackEntity track){
        Document document = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String startStringGetResponseImage = "http://37.140.241.224:5000/?w=700&h=500&z=";
        String zoom;
        String lineResponseCoordinates = "&line=coords:";
        List<Double> latitude = new ArrayList<>();
        List<Double> longitude = new ArrayList<>();
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse(new InputSource(new StringReader(track.getGpx())));
        } catch (Exception e) {
            System.out.println(123);
        }
        Node node = document.getFirstChild();
        NodeList nodeList = document.getElementsByTagName("trkpt");

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (i == 0) {
                latitude.add(Double.valueOf(nodeList.item(i).getAttributes().getNamedItem("lat").getNodeValue()));
                longitude.add(Double.valueOf(nodeList.item(i).getAttributes().getNamedItem("lon").getNodeValue()));
                lineResponseCoordinates = lineResponseCoordinates + nodeList.item(i).getAttributes().getNamedItem("lat").getNodeValue() + "," + nodeList.item(i).getAttributes().getNamedItem("lon").getNodeValue() + ";" + nodeList.item(i + 1).getAttributes().getNamedItem("lat").getNodeValue() + "," + nodeList.item(i + 1).getAttributes().getNamedItem("lon").getNodeValue() + "|width:7|color:%235D3EA8";
            } else if(!nodeList.item(i).getNodeValue().isEmpty()) {
                lineResponseCoordinates = lineResponseCoordinates + "&line=coords:" + nodeList.item(i - 1).getAttributes().getNamedItem("lat").getNodeValue() + "," + nodeList.item(i - 1).getAttributes().getNamedItem("lon").getNodeValue() + ";" + nodeList.item(i).getAttributes().getNamedItem("lat").getNodeValue() + "," + nodeList.item(i).getAttributes().getNamedItem("lon").getNodeValue() + "|width:7|color:%235D3EA8";
                latitude.add(Double.valueOf(nodeList.item(i).getAttributes().getNamedItem("lat").getNodeValue()));
                longitude.add(Double.valueOf(nodeList.item(i).getAttributes().getNamedItem("lon").getNodeValue()));
            }
        }
        Double latiDifference = Math.abs(Collections.max(latitude) - Collections.min(latitude));
        Double longiDifference = Math.abs(Collections.max(longitude) - Collections.min(longitude));
        zoom = latiDifference > longiDifference ? getZoomVariable(latiDifference) : getZoomVariable(longiDifference);
        return startStringGetResponseImage + zoom + lineResponseCoordinates;
    }

    private String getZoomVariable(Double latiLongiDifference) {
        if (latiLongiDifference > 0 & latiLongiDifference < 0.001) {
            return "19";
        } else if (latiLongiDifference > 0.001 & latiLongiDifference < 0.005) {
            return "18";
        } else if (latiLongiDifference >= 0.005 & latiLongiDifference < 0.01) {
            return "17";
        } else if (latiLongiDifference >= 0.01 & latiLongiDifference < 0.03) {
            return "16";
        } else if (latiLongiDifference >= 0.03 & latiLongiDifference < 0.1) {
            return "15";
        } else {
            return "10";
        }
    }
}

