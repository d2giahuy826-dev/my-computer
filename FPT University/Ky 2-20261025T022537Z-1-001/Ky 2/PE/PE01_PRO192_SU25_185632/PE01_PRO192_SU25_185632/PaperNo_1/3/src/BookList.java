
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


class BookList extends ArrayList<Book> { 
      
    public void addBook(Book book){
           this.add(book);    
    }
    public String getTitleById(int id){
        for (int i = 0 ;i < this.size(); i++){
            Book b = this.get(i);
            if(b.getId()== id){
               return b.getTitle().toUpperCase();
        }     
    }
        return "N/A";     
  }
    public BookList getBookListByAuthor(String author){
        BookList b1 = new BookList();
        for (int i =0 ;i < this.size() ; i++ ){
            Book b = this.get(i);
            if(b.getAuthor().equalsIgnoreCase(author)){
            b1.add(b);
            }
        }
        return b1;
    }
            
}