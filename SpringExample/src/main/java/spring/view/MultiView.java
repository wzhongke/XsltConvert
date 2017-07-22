package spring.view;

import org.springframework.web.servlet.view.JstlView;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Created by admin on 2016/11/1.
 */
public class MultiView extends JstlView{
    public boolean checkResource(Locale locale) throws Exception {
        System.out.println("paths: " + Paths.get(this.getServletContext().getRealPath("/")+getUrl()));
        return Files.exists(Paths.get(this.getServletContext().getRealPath("/")+getUrl()));
    }
}
