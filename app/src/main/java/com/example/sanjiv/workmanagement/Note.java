package com.example.sanjiv.workmanagement;

/**
 * Created by sanjiv on 1/1/2018.
 */

public class Note {
    private String Category;
    private String Title;
    private String Description;
    public  Note()
    {

    }

   public Note(String Category, String Title, String Description)
   {
       this.Category= Category;
       this.Title=Title;
       this.Description=Description;
   }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
