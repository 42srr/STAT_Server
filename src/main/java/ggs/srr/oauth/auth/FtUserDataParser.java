package ggs.srr.oauth.auth;

import ggs.srr.domain.user.ft.FtUser;
import ggs.srr.oauth.auth.dto.Image;
import ggs.srr.oauth.client.Client;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class FtUserDataParser {

    public FtUser parseUser(ResponseEntity<HashMap> responseEntity, Client client){
        HashMap<String, Object> body = responseEntity.getBody();

        int fdId = Integer.parseInt(body.get("id").toString());
        String intraId = body.get("login").toString();
        String email = body.get("email").toString();
        String url = body.get("url").toString();
        int wallet = Integer.parseInt(body.get("wallet").toString());
        int collectionPoint = Integer.parseInt(body.get("correction_point").toString());
        double level = getUserLevel(body.get("cursus_users"));
        Image image = getImageDto(body.get("image"));
        return new FtUser(fdId, intraId ,email, url, wallet, collectionPoint, level, image);
    }

    private double getUserLevel(Object cursusUsers){
        ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>)  cursusUsers;
        HashMap<String, Object> cardetData = data.get(1);
        return Double.parseDouble(cardetData.get("level").toString());
    }

    private Image getImageDto(Object image){
        if (image == null)
            return null;
        HashMap<String, Object> data = (HashMap<String, Object>) image;
        HashMap<String, String> versions = (HashMap<String, String>)data.get("versions");
        Image imageDto = new Image();
        imageDto.setLarge(versions.get("large"));
        imageDto.setMidium(versions.get("medium"));
        imageDto.setSmall(versions.get("small"));
        imageDto.setMicro(versions.get("micro"));
        return imageDto;
    }
}
