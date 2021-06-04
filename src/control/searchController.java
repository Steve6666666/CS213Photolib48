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
import model.*;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * control searching photos page
 * @author Xiyue Zhang
 * @author Chuxu Song
 */
public class searchController implements LogoutController {

    @FXML
    TextField Date1,Date2,key1,key2,value1,value2,newName;
    @FXML
    ComboBox<String> comboBox;
    @FXML
    Button Cancel,Create,Search;

    @FXML
    ListView listview;


    /**
     * The list of Image
     */
    public static ArrayList<Image> ImagerResult =new ArrayList<>();
    /**
     * ObervableList which contain imageView
     */
    public ObservableList<ImageView> observableList;
    /**
     * current User
     */
    public User user=Photos.driver.getUser(userName);
    /**
     * current user name
     */
    public static String userName;
    /**
     * currnet album list
     */
    public List<Album> albumList=Photos.driver.getUser(userName).albumList;
    //public Image  selected;


    /**
     * Determine whether the user entered tag to search
     * @return true user didn't input any Tag value to search
     */
    public boolean tagisEmpty(){
        return key1.getText().isEmpty() && key2.getText().isEmpty() && value1.getText().isEmpty() && value2.getText().isEmpty();
    }

    /**
     * Check if tag1 is legal
     * @return true if user enter info in both key1 and value1
     */
    public boolean legaltag1(){
        return !key1.getText().isEmpty() && !value1.getText().isEmpty();
    }

    /**
     * Check if tag2 is legal
     * @return true if user enter info in both key2 and value2
     */
    public boolean lagaltag2(){
        return !key2.getText().isEmpty() && !value2.getText().isEmpty();
    }

    /**
     * when user press search button, determine which kind of data User want to use for search
     */
    public void setSearch( )  {
        ImagerResult.clear();
        listview.refresh();
        if(Date1.getText().isEmpty()&&Date2.getText().isEmpty()&&tagisEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty search");
            alert.setHeaderText("Please Enter Tag, Date to Search!");
            alert.show();
        }
        if(!(Date1.getText().isEmpty()&&Date2.getText().isEmpty())){
            searchByDate();
        }
        if(!tagisEmpty()){
            if(legaltag1()&&lagaltag2()){
                searchByTag(1);
            }else if(legaltag1()){
                searchByTag(2);
            }else if (lagaltag2()){
                searchByTag(3);
            }else{
                searchByTag(4);
            }
        }
        Create.setDisable(false);
    }

    /*
     *
     * use when User enter Tag key and Tag value
     * @param dir
     * dir=1 when User want to search image with two tag
     * dir=2 when User want to search with key1 and value1
     * dir=3 when User want to search with key2 and value2
     * dir=4 when User didn't input both key and value for a tags
     */
    public void searchByTag(int dir){
        switch(dir){
            case 1->{
                String value = comboBox.getSelectionModel().getSelectedItem();
          //      comboBox.setValue(value);
                if(value==null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No Relation");
                    alert.setHeaderText("Please choice relation!");
                    alert.show();
                }
                else if(value.equals("And")) {
                    String a=key1.getText();
                    String b = value1.getText();
                    String c= key2.getText();
                    String d= value2.getText();
                    for(Album album: albumList){
                        for(Image image:album.imageList){
                            boolean jjj =false;
                            boolean fff= false;
                            for(Tag t: image.tags){
                                if(t.name.equals(a)&& t.value.equals(b)){
                                    jjj=true;
                                }
                                if(t.name.equals(c)&& t.value.equals(d)){
                                    fff=true;
                                }
                            }
                            if(jjj&&fff){
                                if(!ImagerResult.contains(image))
                                    ImagerResult.add(image);
                            }
                        }
                    }if (ImagerResult.isEmpty()){

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Empty");
                        alert.setHeaderText("NO Result Match!");
                        alert.show();
                        update();
                    }else{
                        update();
                    }
                }else if(value.equals("Or")){

                    String a=key1.getText();
                    String b = value1.getText();
                    String c= key2.getText();
                    String d= value2.getText();
                    for(Album album: albumList){
                        for(Image image:album.imageList){
                            for(Tag t: image.tags){
                                if(t.name.equals(a)&&t.value.equals(b)||t.name.equals(c)&&t.value.equals(d)){
                                    if(!ImagerResult.contains(image))
                                        ImagerResult.add(image);
                                }
                            }
                        }
                    }if (ImagerResult.isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Empty");
                        alert.setHeaderText("NO Result Match!");
                        alert.show();
                        update();
                    }else{
                        update();
                    }
                }
            }
            case 2->{
                String a=key1.getText();
                String b = value1.getText();
                for(Album album: albumList){
                    for(Image image:album.imageList){
                        for(Tag t: image.tags){
                            if(t.name.equals(a)&&t.value.equals(b)){
                                if(!ImagerResult.contains(image))
                                    ImagerResult.add(image);
                            }
                        }
                    }
                }if (ImagerResult.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Empty");
                    alert.setHeaderText("NO Result Match!");
                    alert.show();
                    update();
                }else{
                    update();
                }
            }
            case 3->{

                String a=key2.getText();
                String b = value2.getText();
                for(Album album: albumList){
                    for(Image image:album.imageList){
                        for(Tag t: image.tags){
                            if(t.name.equals(a)&&t.value.equals(b)){
                                if(!ImagerResult.contains(image))
                                    ImagerResult.add(image);
                            }
                        }
                    }
                }if (ImagerResult.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Empty");
                    alert.setHeaderText("NO Result Match!");
                    alert.show();
                    update();
                }else{
                    update();
                }

            }
            case 4->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Wrong input type");
                alert.setHeaderText("Please Enter Key and Value!");
            }
        }
    }

    /**
     * User search image by a date range.
     */
    public void searchByDate()  {
        //StringBuffer builder= new StringBuffer().append(date.getText());
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
        if(!Date1.getText().isEmpty()&&Date2.getText().isEmpty()){//image after date1
            try {
                Date dd =fmt.parse(Date1.getText());
                for (Album album:albumList){
                    for(Image image:album.imageList){
                        if(image.date.after(dd)){
                            if(!ImagerResult.contains(image))
                                ImagerResult.add(image);
                        }
                    }
                }if (ImagerResult.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Empty");
                    alert.setHeaderText("NO Result Match!");
                    alert.show();
                    update();
                }else {
                update();

                }
            } catch (ParseException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("wrong format");
                alert.setHeaderText("enter date range in correct format!");
                alert.show();
            }
            Date1.clear();
        }else if(Date1.getText().isEmpty()&&!Date2.getText().isEmpty()){//image before date2
            try {
                Date dd =fmt.parse(Date2.getText());
                for (Album album:albumList){
                    for(Image image:album.imageList){
                        if(image.date.before(dd)){
                            if(!ImagerResult.contains(image))
                                ImagerResult.add(image);
                        }
                    }
                }if (ImagerResult.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Empty");
                    alert.setHeaderText("NO Result Match!");
                    alert.show();
                    update();
                }else {
                    update();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("wrong format");
                alert.setHeaderText("enter date range in correct format!");
                alert.show();
            }
            Date2.clear();
        }else if(!Date1.getText().isEmpty()&&!Date2.getText().isEmpty()){//image after date1 before date2
            try {
                Date dd =fmt.parse(Date1.getText());
                Date ddd= fmt.parse(Date2.getText());
                 if(dd.after(ddd)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Wrong Date");
                    alert.setHeaderText("StartDate is after Enddate!");
                    alert.show();
                }else {
                    for (Album album : albumList) {
                        for (Image image : album.imageList) {
                            String date=fmt.format(image.date);
                            Date date1=fmt.parse(date);
                           // System.out.println(date1.toString().equals(dd.toString()));
                            if ((date1.after(dd) && date1.before(ddd))||date1.equals(dd)||date1.equals(ddd)) {
                                if(!ImagerResult.contains(image))
                                    ImagerResult.add(image);
                            }
                        }
                    }
                }
                if (ImagerResult.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Empty");
                    alert.setHeaderText("NO Result Match!");
                    alert.show();
                    update();
                }else {
                    //System.out.println(ImagerResult.size());
                    update();
                }
            } catch (ParseException e) {
                //e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("wrong format");
                alert.setHeaderText("enter date range in correct format!");
                alert.show();
            }
            Date1.clear();
            Date2.clear();
        }

    }

    /**
     * User press cancel button to come back userPage
     * @param event cancel action
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void cancel(ActionEvent event) throws IOException, ClassNotFoundException {
        UserController.username = userName;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserPage.fxml"));
        Parent sceneManager = (Parent) fxmlLoader.load();
        UserController userController = fxmlLoader.getController();
        Scene adminScene = new Scene(sceneManager);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        userController.open(appStage);
        appStage.setScene(adminScene);
        appStage.show();
    }


    /**
     * User press button and create album with their searching result
     * @throws IOException
     */
    public void create() throws IOException {
        if(ImagerResult.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty search");
            alert.setHeaderText("List is empty!");
            alert.show();
            return;
        }
        if(newName.getText().isEmpty()|| user.existAlbum(newName.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty/Duplicated name");
            alert.setHeaderText("Please enter a valid Album name!");
            alert.show();
            return;
        }
        Album album=new Album(newName.getText());
        album.imageList=ImagerResult;
        user.albumList.add(album);
        User.save(Photos.driver.usersList);
        user.save(user.albumList,userName);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Created Album");
        alert.setHeaderText("Successfully created album!");
        alert.show();

    }

//    public void open() {
//
//        Create.setDisable(true);
//        ListView.setOnMouseClicked(mouseEvent -> {
//            if (!ListView.getSelectionModel().isEmpty()) {
//                Create.setDisable(false);
//                selected= observableList.get(ListView.getSelectionModel().getSelectedIndex());
//            }
//        });
//    }

    /**
     * update ImagerResult and show it in Listview  every time when User make some action
     *
     */
    public void update(){
        listview.refresh();
        List<javafx.scene.image.ImageView> iii= new ArrayList<ImageView>();
        for(Image i:ImagerResult){
            File file= new File(i.path);
            javafx.scene.image.Image imt= new javafx.scene.image.Image(file.toURI().toString(),50,50,false,false);
            javafx.scene.image.ImageView imageView= new ImageView();
            imageView.setImage(imt);
            iii.add(imageView);
        }
        observableList= FXCollections.observableList(iii);
        listview.setItems(observableList);
        listview.setCellFactory(param -> new ListCell<ImageView>() {
            private List<String> Caption = new ArrayList<String>();

            @Override
            public void updateItem(ImageView name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                } else {
                    for (Image i : ImagerResult) {
                        StringBuilder b = new StringBuilder();
                        b.append("file:");
                        b.append(i.path);
                        String a = String.valueOf(b);

                        //System.out.println(a.equals(name.getImage().getUrl()));
                        //System.out.println(name.getImage().getUrl().toString());
                        //System.out.println(a);
                        if (name.getImage().getUrl().equals(a)) {
                            setText(i.caption);
                        }
                    }
                    setGraphic(name);
                }
            }
        });
    }

}
