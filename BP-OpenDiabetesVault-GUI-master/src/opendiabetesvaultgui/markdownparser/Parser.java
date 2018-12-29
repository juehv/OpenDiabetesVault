/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.markdownparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.markdown.core.MarkdownLanguage;

/**
 * help class to handle md to html parsing.
 *
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
public class Parser {

    /**
     * takes an md file given as a filepath, returns the given file as html code
     * as a String.
     *
     * @param filePath path of the file to parse
     * @return html code as a String
     * @throws FileNotFoundException Exception if the file cannot be found
     */
    public String mdParse(final String filePath)
            throws FileNotFoundException, IOException {
        String fileContent = readFile(filePath);
        fileContent = parseString(fileContent);
        return fileContent;
    }

    /**
     * takes md code as a String and returns it as html code.
     *
     * @param markupContent the md code
     * @return the html code as a String
     */
    private String parseString(final String markupContent) {
        MarkupParser markupParser = new MarkupParser();
        markupParser.setMarkupLanguage(new MarkdownLanguage());
        String htmlContent = markupParser.parseToHtml(markupContent);
        return htmlContent;
    }

    /**
     * takes a filepath, reads the given file and parses it into a String with
     * whitespaces.
     *
     * stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java
     * www.leveluplunch.com/java/examples/convert-java-util-stream-to-string/
     *
     * @param filePath path of the file to read
     * @return the filecontent as a String
     * @throws FileNotFoundException Exception if the file cannot be found
     */
    private String readFile(String filePath) throws IOException {
        String returnValue = "";
        InputStream in = getClass().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        while ((line = reader.readLine()) != null) {
            returnValue += line + "\n";
        }
        reader.close();
        return returnValue;
    }

}
