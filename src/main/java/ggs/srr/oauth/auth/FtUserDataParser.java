package ggs.srr.oauth.auth;

import ggs.srr.domain.user.FtUser;
import ggs.srr.domain.user.Role;
import ggs.srr.oauth.auth.dto.Image;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FtUserDataParser {

    public FtUser parseUser(ResponseEntity<HashMap> responseEntity){
        HashMap<String, Object> body = responseEntity.getBody();

        System.out.println(body);
        int fdId = Integer.parseInt(body.get("id").toString());
        String intraId = body.get("login").toString();
        Role role = getRole(intraId);
        int wallet = Integer.parseInt(body.get("wallet").toString());
        int collectionPoint = Integer.parseInt(body.get("correction_point").toString());
        double level = getUserLevel(body.get("cursus_users"));
        String image = getImageDto(body.get("image")).getSmall();
        return new FtUser(fdId, intraId , role, wallet, collectionPoint, level, image);
    }

    private double getUserLevel(Object cursusUsers){
        double level = 0L;
        ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>)  cursusUsers;

        for(HashMap<String , Object> datum : data) {
            int cursusId = (int) datum.get("cursus_id");
            if (cursusId == 21){
                level = Double.parseDouble(datum.get("level").toString());
                System.out.println("level = " + level);
            }

        }
        return level;
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

    private Role getRole(String intraId){
        List<String> adminList = List.of("joojeon", "jajo");
        if (adminList.contains(intraId)){
            return Role.ADMIN;
        }
        return Role.CADET;
    }
}
