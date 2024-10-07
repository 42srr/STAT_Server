package ggs.srr.domain.user;

import ggs.srr.oauth.auth.dto.Image;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class FtUser {

   @Id @GeneratedValue
   private long id;

   private int fdId;
   private String intraId;
   private String role;
   private String email;
   private String url;
   private int wallet;
   private int collectionPoint;
   private double level;
   private String image;

   private String jwtAccessToken;
   private String jwtRefreshToken;

   public FtUser() {
   }

   public FtUser(int fdId, String intraId, String role, String email, String url,
                 int wallet, int collectionPoint, double level, String image) {
      this.fdId = fdId;
      this.role = role;
      this.intraId = intraId;
      this.email = email;
      this.url = url;
      this.wallet = wallet;
      this.collectionPoint = collectionPoint;
      this.level = level;
      this.image = image;
   }

   public void setJwtToken(String jwtAccessToken, String jwtRefreshToken) {
      this.jwtAccessToken = jwtAccessToken;
      this.jwtRefreshToken = jwtRefreshToken;
   }
}
