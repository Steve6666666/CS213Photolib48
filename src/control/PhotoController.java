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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Control the Album Photo Page
 * @author Xiyue Zhang
 * @author Chuxu Song
 */
public class PhotoController {
    @FXML
    Button previous, next, add, remove, back, copy, move, edit;
    @FXML
    ListView<ImageView> ImageList;
    @FXML
    ImageView Display;
    @FXML
    TextArea date, name, tags;

    /**
     * current Album
     */
    public static Album curAlbum;
    /**
     * current username
     */
    public static String userName;
    /**
     * current User
     */
    public User user = Photos.driver.cur;
    public static Admin admin = Photos.driver;
    /**
     * current Image list
     */
    public List<Image> images;
    public ObservableList<Image> obsList;
    public Image selected;
    /**
     * ImageView in listview which selected by User
     */
    public ImageView selected1;
    String path;
    /**
     * ObservableList contain ImageView
     */
    public ObservableList<ImageView> i;

    /**
     * when User load photoPage, get current user and album.
     * put all image in album to image list
     */
    public void open() {
        if (curAlbum.imageList == null) {
            curAlbum.imageList = new ArrayList<>();
        }
        this.images = curAlbum.imageList;
        //System.out.println(images.size());
        if (!images.isEmpty()) {
            update();
        }
        previous.setDisable(true);
        next.setDisable(true);
        remove.setDisable(true);
        copy.setDisable(true);
        edit.setDisable(true);
        move.setDisable(true);
        ImageList.setOnMouseClicked(mouseEvent -> {
            if (!ImageList.getSelectionModel().isEmpty()) {
                previous.setDisable(false);
                next.setDisable(false);
                remove.setDisable(false);
                copy.setDisable(false);
                edit.setDisable(false);
                move.setDisable(false);
                display();
            }});
    }

    /**
     * User press add button to choose image and add it to current album list
     * @throws IOException
     */
    public void add() throws IOException {
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(extFilterJPG);
        File imgfile = chooser.showOpenDialog(null);
        if (imgfile == null) {
            return;
        } else {
            String filepath = imgfile.getAbsolutePath();
            Image newPhoto;
            if (userName.equalsIgnoreCase("stock")) {
                int index;
                if (filepath.contains("stock")) {
                    index = filepath.indexOf("stock");
                    String newfilepath = filepath.substring(index);
                    //System.out.println(newfilepath);
                    Image newPhoto2 = new Image(imgfile, newfilepath);
                    newPhoto2.isStock = true;
                    boolean duplicate2 = false;
                    for (Image i : curAlbum.imageList) {
                        String newpath = i.path.substring(index);
                        if (newpath.equalsIgnoreCase(newfilepath)) {
                            duplicate2 = true;
                        }
                    }
                    if (duplicate2 == true) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Duplicate");
                        alert.setHeaderText("Image already exists!");
                        Optional<ButtonType> Clicked = alert.showAndWait();
                        if (Clicked.get() == ButtonType.OK) {
                            alert.close();
                        } else {
                            alert.close();
                        }
                        return;
                    }
                    for(Album album:user.albumList){
                        if(album.getImageByPath(filepath)!=null){
                            curAlbum.addImage(album.getImageByPath(filepath));
                            update();
                            user.save(user.albumList, userName);
                            return;
                        }
                    }
                    curAlbum.addImage(newPhoto2);
                } else {
                    for(Album album:user.albumList){
                        if(album.getImageByPath(filepath)!=null){
                            curAlbum.addImage(album.getImageByPath(filepath));
                            update();
                            user.save(user.albumList, userName);
                            return;
                        }
                    }
                    newPhoto = new Image(imgfile, filepath);
                    curAlbum.addImage(newPhoto);
                }
            }
            else {
                boolean duplicate = false;
                for (Image i : curAlbum.imageList) {
                    if (i.path.equalsIgnoreCase(filepath)) {
                        duplicate = true;
                    }
                }
                if (duplicate == true) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate");
                    alert.setHeaderText("Image already exists!");
                    Optional<ButtonType> Clicked = alert.showAndWait();
                    if (Clicked.get() == ButtonType.OK) {
                        alert.close();
                    } else {
                        alert.close();
                    }
                    return;
                }
                for(Album album:user.albumList){
                    if(album.getImageByPath(filepath)!=null){
                        curAlbum.addImage(album.getImageByPath(filepath));
                        update();
                        user.save(user.albumList, userName);
                        return;
                    }
                }
                newPhoto = new Image(imgfile, filepath);
                curAlbum.addImage(newPhoto);
            }
            update();
            user.save(user.albumList, userName);
        }

    }

    /**
     * User press remove button and remove current image from current album
     * @throws IOException
     */
    public void remove() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Sure you want to delete Image?");
        Optional<ButtonType> Clicked = alert.showAndWait();
        if (Clicked.get() == ButtonType.OK) {
            curAlbum.removeImageByPath(path);
            update();
            user.save(user.albumList, userName);
            display();
        } else {
            alert.close();
        }
    }

    /**
     * User press back button and com back to userPage
     * @param event Go to previous page Action
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void back(ActionEvent event) throws IOException, ClassNotFoundException {
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
     *
     * User press copy button and go to copy photo page
     * @param event Copy Action
     * @throws IOException
     */
    public void copy(ActionEvent event) throws IOException {
        PhotoCopyController.path = path;
        PhotoCopyController.username = userName;
        PhotoCopyController.curAlbum = curAlbum;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/photoCopyPage.fxml"));
        Parent sceneManager = (Parent) fxmlLoader.load();
        Scene adminScene = new Scene(sceneManager);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(adminScene);
        appStage.setTitle("PhotoCopyPage");
        appStage.show();
    }

    /*
     * User press move button and go to photo move page
     * @param event Move Action
     * @throws IOException
     */
    public void move(ActionEvent event) throws IOException {
        PhotoMoveController.path = path;
        PhotoMoveController.username = userName;
        PhotoMoveController.curAlbum = curAlbum;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/photoMovePage.fxml"));
        Parent sceneManager = (Parent) fxmlLoader.load();
        Scene adminScene = new Scene(sceneManager);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(adminScene);
        appStage.setTitle("PhotoMovePage");
        appStage.show();

    }

    /**
     * open photo edit page
     * @param event edit action
     * @throws IOException
     */
    public void edit(ActionEvent event) throws IOException {
        photoEditController.list1 = curAlbum.imageList;
        photoEditController.imageView = selected1;
        photoEditController.username = userName;
        photoEditController.curAlbum = curAlbum;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/photoEditPage.fxml"));
        Parent root = fxmlLoader.load();
        photoEditController photoEditController = fxmlLoader.getController();
        Scene editscene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(editscene);
        photoEditController.open();
        //photoEditController.list1=curAlbum.imageList;
        appStage.setTitle("PhotoEditPage");
        appStage.show();
    }

    /**
     * User press previous button and Imageview will show previous image in the display area
     */
    public void previous() {
        if (ImageList.getSelectionModel().getSelectedIndex() != 0) {
            ImageList.getSelectionModel().selectPrevious();
        } else
            ImageList.getSelectionModel().selectLast();
        display();
    }

    /**
     * User press next button and Imageview will show next image in the display area
     */
    public void next() {
        if (ImageList.getSelectionModel().getSelectedIndex() != images.size() - 1) {
            ImageList.getSelectionModel().selectNext();
        } else
            ImageList.getSelectionModel().selectFirst();
        display();
    }

    /**
     * update the Imagelist and change every time when user make some action about album photo list
     */
    public void update() {
        List<ImageView> iv = new ArrayList<ImageView>();
        for (Image i : images) {
            if(userName.equals("stock")&& i.isStock){
                        String abs = i.file.getAbsolutePath();
                        i.path=abs;
            }
            File test = new File(i.path);
            javafx.scene.image.Image imt = new javafx.scene.image.Image(test.toURI().toString(), 300, 300, false, false);
            //arrayreali.add(new javafx.scene.image.Image(test.toURI().toString(),50,50,false,false));
            ImageView imageview = new ImageView();
            imageview.setFitHeight(50);
            imageview.setPreserveRatio(true);
            imageview.setImage(imt);
            iv.add(imageview);
        }
        i = FXCollections.observableList(iv);
        ImageList.setItems(i);
        ImageList.setCellFactory(param -> new ListCell<ImageView>() {
            private List<String> Caption = new ArrayList<String>();

            @Override
            public void updateItem(ImageView name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                } else {
                    for (Image i : curAlbum.imageList) {
                        StringBuilder b = new StringBuilder();
                        b.append("file:");
                        b.append(i.path);
                        String a = String.valueOf(b);
                        convert(a);

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

    /**
     *
     * input image path s, and if computer have window system, it will convert file path that can be readble by java.
     * @param s Image path
     * @return path for image class
     */
    public String convert(String s) {
        Properties props = System.getProperties();
        String osName = props.getProperty("os.name");
        String c = " ";
        String d = "/";
        if (osName.equals("Windows 10")) {
            s.replaceAll(c, d);
        }

        return s;
    }


    /**
     * Dipalay the image to the display area with preserved ratio
     */
    public void display() {
        tags.clear();
        name.clear();
        if(ImageList.getSelectionModel().isEmpty()){
            tags.clear();
            date.clear();
            name.clear();
            Display.setImage(null);
            return;
        }
        selected1 = i.get(ImageList.getSelectionModel().getSelectedIndex());
        path = selected1.getImage().getUrl().substring(5);
        Image curImage = curAlbum.getImageByPath(path);
       // System.out.println(curImage.path);
        date.setText(curImage.date.toString());
        Display.setImage(selected1.getImage());
        if (curImage.caption != null) {
            //System.out.println(curImage.caption);
            name.setText(curImage.caption);
        }
        if (!curImage.tags.isEmpty()) {
            StringBuilder s = new StringBuilder();
            for (Tag tag : curImage.tags) {
                s.append(tag.name + ":" + tag.value + " ");
            }
            tags.setText(s.toString());
        }
    }
}


