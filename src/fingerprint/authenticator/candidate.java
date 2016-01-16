/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.authenticator;

import java.sql.Date;

/**
 *
 * @author Developer
 */
public class candidate {

    public String getAppno() {
        return appno;
    }

    public void setAppno(String appno) {
        this.appno = appno;
    }


    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    String appno;
    byte[] photo;

    public candidate(String appno,byte[] photo) {
        this.appno = appno;
       
        this.photo = photo;
  
    }

//        public candidate(int appno, int formno, String cname, Date dob, int cat, int sex, String fname, Date edate, int shift, byte[] photo, int rollno) {
//        this.appno = appno;
//        this.formno = formno;
//        this.cname = cname;
//        this.dob = dob;
//        this.cat = cat;
//        this.sex = sex;
//        this.fname = fname;
//        this.edate = edate;
//        this.shift = shift;
//        this.photo = photo;
//        this.rollnumber = rollno;
//    }
}
