/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.authenticator;

import java.util.prefs.Preferences;

public class UsingReg {
  public static final String REALKEY= "com.rgagnon.foo";

  public static void main(String[] args){
     new UsingReg().doit();
  }

  public void doit() {
    // write into HKCU\Software\Javasoft\Prefs\com.rgagnon.foo
    Preferences p = Preferences.userRoot();
    p.put(REALKEY, "bar");

    // read back from HKEY_CURRENT_USER
    System.out.println(p);
    System.out.println(p.get(REALKEY, "HKCU houston we have a problem"));

    // write into HKLM\Software\Javasoft\Prefs\com.rgagnon.foo
    p = Preferences.systemRoot();
    p.put(REALKEY, "barbar");

    // read back from HKEY_LOCAL_MACHINE
    System.out.println(p);
    System.out.println(p.get(REALKEY, "HKLM houston we have a problem"));
  }
}