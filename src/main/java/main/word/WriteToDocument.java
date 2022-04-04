package main.word;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class WriteToDocument {

    public WriteToDocument(List<String> scores){

        try{
            writeDoc(scores);
        } catch (FileNotFoundException e){
            e.addSuppressed(e);
        }

    }

    private void writeDoc(List<String> scores) throws FileNotFoundException {
        XWPFDocument d = new XWPFDocument();

        int counter = 1;

        try(OutputStream output = new FileOutputStream("test.docx")){
            XWPFParagraph para = d.createParagraph();
            XWPFRun first = para.createRun();
            XWPFRun second = para.createRun();
            XWPFRun third = para.createRun();
            XWPFRun fourth = para.createRun();
            for(int i = scores.size(); i > 0; i--){
                if(counter <= 10){
                    first.setColor("CC0099");
                    first.setText(counter + ": " + scores.get(i-1));
                    first.addBreak();
                    counter++;
                } else if (counter <= 20) {
                    second.setColor("CC99FF");
                    second.setText(counter + ": " + scores.get(i-1));
                    second.addBreak();
                    counter++;
                } else if (counter <= 30){
                    third.setColor("33CCFF");
                    third.setText(counter + ": " + scores.get(i-1));
                    third.addBreak();
                    counter++;
                } else {
                    fourth.setColor("00CC00");
                    fourth.setText(counter + ": " + scores.get(i-1));
                    fourth.addBreak();
                    counter++;
                }

                System.out.println(counter);


            }
            d.write(output);
        } catch (Exception e){
            System.out.println("File failed to build");
        }
    }

}
