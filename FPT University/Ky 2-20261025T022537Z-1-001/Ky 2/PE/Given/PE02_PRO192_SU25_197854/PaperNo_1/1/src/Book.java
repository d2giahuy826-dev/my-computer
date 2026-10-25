/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GIA HUY
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private int pageCount;

    public Book(int id, String title, String author, int pageCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        
        if ( pageCount < 100) {
            this.pageCount = 100;
        }else {
        this.pageCount = pageCount;
        }
    }
    public String getTitle(){
        if ( title!= null && title.length() >= 6){
            return title.toUpperCase();
        }else {
            return "N/A";
        }
    }

    public String getAuthor(){
        if ( author.isEmpty()){
            return "N/A";
        }else {
            return author.toUpperCase();
        }
    }
    @Override
    public String toString() {
        return "Book{" + id + "," + getTitle() + " , " + getAuthor() + " , " + pageCount + '}'; 
    }
    
}
