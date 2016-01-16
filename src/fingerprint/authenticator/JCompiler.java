/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.authenticator;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
 
/**
 *
 * @author Babji Prashanth, Chetty
 */
public class JCompiler {
    public static void main(String[] args) throws IOException {
      File dir = new File("directory-path");
      File[] javaFiles = dir.listFiles(
              new FilenameFilter() {
                  public boolean accept(File file, String name) {
                      return name.endsWith(".java");
                  }
              });
        
      JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
      DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
      StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(diagnostics, Locale.GERMANY, Charset.forName("UTF-8"));
      Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(javaFiles));
      javaCompiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits).call();
        
      for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
          System.out.format("Error on line %d in %d%n", diagnostic.getLineNumber(), diagnostic.getSource().toString());
      }        
 
      fileManager.close();
    }
}