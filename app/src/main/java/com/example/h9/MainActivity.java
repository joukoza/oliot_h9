package com.example.h9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    Theaters theaters = Theaters.getInstance();
    Spinner spinner;
    EditText startDate;
    EditText endDate;
    EditText movieText;
    String selectedTheater;
    String movieName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        startDate = (EditText) findViewById(R.id.start);
        endDate = (EditText) findViewById(R.id.end);
        movieText = (EditText) findViewById(R.id.textMovie);
        UpdateSpinner();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        selectedTheater = parent.getItemAtPosition(pos).toString();
        System.out.println(parent.getItemAtPosition(pos).toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void readXML(View v) {
        Document doc =  getXML("https://www.finnkino.fi/xml/TheatreAreas/");
        NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");

        for (int i = 0; i < nList.getLength(); i++){
            Node node = nList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){

                Element element = (Element) node;
                String elementName = element.getElementsByTagName("Name").item(0).getTextContent();
                if (elementName.contains(":")){
                    int elementId = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());
                    theaters.addTheater(elementId, elementName);
                }
            }
        }
        UpdateSpinner();
    }

    private Document getXML(String url){
        Document doc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(url);
            doc.getDocumentElement().normalize();
            return doc;

        } catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        catch (SAXException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("########LÄPI#####");
        }
        return doc;

    }

    public void searchMovies(View v){
        String startTime = startDate.getText().toString();
        // String endTime = endDate.getText().toString();

        String[] startParts = startTime.split("T");
        // String[] endParts = endTime.split("T");

        String[] startDates = startParts[0].split("-");
        String[] startTimes = startParts[1].split(":");

        //String[] endDates = endParts[0].split("-");
        //String[] endTimes = endParts[1].split(":");

        System.out.println(selectedTheater);
        System.out.println(startDates[2] +"."+startDates[1]+ "."+startDates[0]);
        //System.out.println(endDates[2] +"."+endDates[1]+ "."+endDates[0]);

        int theaterId = theaters.getTheaterIdWithName(selectedTheater);
        String searchMovie = movieText.getText().toString();
        String startURL = "https://www.finnkino.fi/xml/Schedule/?area=<id>&dt=<d>.<m>.<y>";
        startURL = startURL.replace("<id>", Integer.toString(theaterId));
        startURL = startURL.replace("<d>", startDates[2]);
        startURL = startURL.replace("<m>", startDates[1]);
        startURL = startURL.replace("<y>", startDates[0]);
        System.out.println(startURL);
        Document doc = getXML(startURL);
        NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

        for (int i = 0; i < nList.getLength(); i++){
            Node node = nList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){

                Element element = (Element) node;
                String elementName = element.getElementsByTagName("Name").item(0).getTextContent();
                if (elementName.contains(":")){
                    int elementId = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());
                    theaters.addTheater(elementId, elementName);
                }
            }
        }

    }

    public void UpdateSpinner(){
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < theaters.getTheaterAmount(); i++){
            names.add(theaters.getTheaterNameWithListId(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}