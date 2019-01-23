package com.example.ashraf.newsapp;
public class NewsItem {
    String Author;
    String WebTitle;
    String SectionName;
    String Date;
    String Url;
    public NewsItem(String Author, String WebTitle,String SectionName,String Date,String Url) {
        this.Author=Author;
        this.WebTitle=WebTitle;
        this.SectionName=SectionName;
        this.Date=Date;
        this.Url=Url;
    }
    public String getAuthor() {return Author;}
    public String getWebTitle() {return WebTitle;}
    public String getSectionName() {return SectionName;}
    public String getDate() {return Date;}
    public String getUrl(){return Url;}
}
