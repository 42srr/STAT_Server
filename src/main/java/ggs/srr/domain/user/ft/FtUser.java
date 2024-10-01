package ggs.srr.domain.user.ft;

import ggs.srr.oauth.auth.dto.Image;
import lombok.Getter;

@Getter
public class FtUser {

   private int fdId;
   private String intraId;
   private String email;
   private String url;
   private int wallet;
   private int collectionPoint;
   private double level;
   private Image image;

   public FtUser(int fdId, String intraId, String email, String url, int wallet, int collectionPoint, double level, Image image) {
      this.fdId = fdId;
      this.intraId = intraId;
      this.email = email;
      this.url = url;
      this.wallet = wallet;
      this.collectionPoint = collectionPoint;
      this.level = level;
      this.image = image;
   }

}
