package control;

import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Image;
import model.Tag;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * control photo Edit Page, includes cap and recap, add and delete tags.
 * @author Xiyue Zhang
 * @author Chuxu Song
 */
public class photoEditController  {

    @FXML
    TextField newCaption,tagKey,tagValue;

    @FXML
    Button add1,add2, delete,back;

    @FXML
    ListView possibleKeylist;
    @FXML ListView preset;
    /**
     *current selected tag
     */
    StringBuilder selected;
    /**
     * current tag key list
     */
    public static List<String> keyarr=new ArrayList<>();
    public static ImageView imageView;
    /**
     * list of image
     */
    public static List<Image> list1;
    public static String username;
    public static Album curAlbum;
    Image cur;
    public User user= Photos.driver.cur;
    /**
     * observablelsit to display tag name and tag value
     */
    ObservableList<StringBuilder> observableList;
    /**
     * observablelist to display key value
     */
    ObservableList<String> observableList2;
    String selected2;

    /**
     * User press add button to add caption with value in newCaption field
     * @throws IOException
     */
    public void setAdd1() throws IOException {
        if(newCaption.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty");
            alert.setHeaderText("Please Enter Caption!");
            alert.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conform Change");
            alert.setHeaderText("Sure you want to caption the Photo:"+newCaption.getText());
            alert.show();
            cur.caption=newCaption.getText();
            update();
            user.save(user.albumList,username);
            newCaption.clear();
        }
    }

    /**
     * Check if there are two tag inputs
     * @return true if the two inputs are both not empty, false otherwise
     */
    public boolean correctTaginput(){//input correct
        return !tagKey.getText().isEmpty() && !tagValue.getText().isEmpty();
    }

    /**
     * User press add button to add tag with value in tagKey and tagValue
     * @throws IOException
     */
    public void setAdd2() throws IOException {
        if(correctTaginput()){
            Tag t = new Tag(tagKey.getText(),tagValue.getText());
            if(cur.tags==null){
                cur.tags=new ArrayList<>();
            }

            boolean flag=false;
            boolean flag2=false;
            for (String s: keyarr) {
                if(s.equals(t.name)){
                    flag=true;
                }
                if (cur.hasTag("location")&&t.name.equalsIgnoreCase("location")){
                    return;
                }
            }
            if(flag==false) keyarr.add(t.name);
            for (Tag tag:cur.tags) {
                if(t.name.equals(tag.name)&&t.value.equals(tag.value)){
                    flag2=true;
                }
            }
            if(flag2==false)   cur.tags.add(t);
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicated tag");
                alert.setHeaderText("Please Enter a new key and value pair!");
                alert.show();
            }
            update();
            user.save(user.albumList,username);
            tagValue.clear();
            tagKey.clear();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not enough data");
            alert.setHeaderText("Please Enter both Tagkey and TagValue!");
            alert.show();
        }
    }

    /**
     * start the controller and initialize the field
     */
    public void open(){
        for(Image i: list1){
            //String path = convert(i.path);
            if(i.path.equalsIgnoreCase(imageView.getImage().getUrl().substring(5))){
                cur=i;
            }
        }
        if (keyarr.isEmpty()){
            keyarr.add("Location");keyarr.add("Name");keyarr.add("Feeling");keyarr.add("Weather");
        }
        for (Tag t:cur.tags) {
            if (!keyarr.contains(t.name))
                keyarr.add(t.name);
        }
        update();
        delete.setDisable(true);
        possibleKeylist.setOnMouseClicked(mouseEvent -> {
            if (!possibleKeylist.getSelectionModel().isEmpty()) {
               delete.setDisable(false);
               selected= observableList.get(possibleKeylist.getSelectionModel().getSelectedIndex());
            }
        });
        preset.setOnMouseClicked(mouseEvent -> {
            if(!preset.getSelectionModel().isEmpty()){
                selected2=observableList2.get(preset.getSelectionModel().getSelectedIndex());
                tagKey.setText(selected2);
            }
    });

    }

    /**
     * User press delete button to delete the current selected tag
     * @throws IOException
     */
    public void setDelete() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Sure you want to delete Tags: " + selected + "?");
        Optional<ButtonType> Clicked = alert.showAndWait();
        if (Clicked.get() == ButtonType.OK) {
            Tag tar=null;
            for(Tag t: cur.tags){
                StringBuilder b = new StringBuilder();
                b.append(t.name+":"+t.value);
                //System.out.println(b.toString());
                //System.out.println(selected.toString());
                if(b.toString().equals(selected.toString())){
                    tar=t;
                }

            }
            if(tar!=null){
                cur.tags.remove(tar);
                update();
            }
        } else {
            alert.close();
        }
        user.save(user.albumList,username);
    }

    /**
     * update the data in possible key list and preset list
     */
    public void update(){
        List<StringBuilder> stringBuilderslist= new ArrayList<StringBuilder>();
        if(!cur.tags.isEmpty()){
            for(Tag t:cur.tags){
                StringBuilder b= new StringBuilder();
                b.append(t.name+":"+t.value);
                stringBuilderslist.add(b);
            }
        }
        observableList= FXCollections.observableList(stringBuilderslist);
        possibleKeylist.setItems(observableList);
        observableList2=FXCollections.observableList(keyarr);
        preset.setItems(observableList2);

    }

    /**
     * user press back button and come back to photoPage
     * @param event back Action
     * @throws IOException
     */
    public void back(ActionEvent event) throws IOException {
        PhotoController.curAlbum=curAlbum;
        PhotoController.userName=username;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PhotoPage.fxml"));
        Parent root = fxmlLoader.load();
        PhotoController photoController = fxmlLoader.getController();
        Scene photoScene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        photoController.open();
        appStage.setScene(photoScene);
        appStage.show();
    }

    /**
     * input image path s, and if computer have window system, it will convert file path that can be readble by java.
     * @param s Image path
     * @return path for image class
     */
    public String convert(String s){
        StringBuilder b = new StringBuilder();
        b.append("file:/");
        b.append(s);
        String a = String.valueOf(b);
        return a;
    }
}
