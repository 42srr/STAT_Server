package ggs.srr.security.authentication.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UserDetails {

    private Long ftServerId;
    private String intraId;
    private int wallet;
    private int correctionPoint;


    private UserDetails(Long ftServerId, String intraId, int wallet, int correctionPoint) {
        this.ftServerId = ftServerId;
        this.intraId = intraId;
        this.wallet = wallet;
        this.correctionPoint = correctionPoint;
    }

    public static UserDetails of(Map<String, Object> map) {
        long ftServerId = Long.parseLong(map.get("id").toString());
        String intraId = map.get("login").toString();
        int wallet = Integer.parseInt(map.get("wallet").toString());
        int correctionPoint = Integer.parseInt(map.get("correction_point").toString());

        return new UserDetails(ftServerId, intraId, wallet, correctionPoint);
    }
}
