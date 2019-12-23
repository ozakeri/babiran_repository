package Models;

/**
 * Created by Tohid on 2/8/2017 AD.
 */

public class Advertising {
    public String id,company_id,category_id,name,visit,company_name,description,products,conditions,email,phone,
    email_public ,phone_public,province_id,city_id,has_image,active,created_at,updated_at,img1,img2,img3,img4,hoze,address,why;

    public Advertising(String id,String company_id,String category_id,String name,String visit,String hoze,String company_name,String description,String products,String conditions,String email,String phone,String
            email_public ,String phone_public,String province_id,String city_id,String has_image,String active,String created_at,String updated_at,String img1,String img2,String img3,String img4,String address){

        this.id=id;
        this.company_id=company_id;
        this.category_id=category_id;
        this.name=name;this.visit=visit;
        this.address = address;
        this.hoze = hoze;
        this.company_name=company_name;this.description=description;this.products=products;
        this.conditions=conditions;this.email=email;this.phone=phone;this.email_public =email_public;this.phone_public=phone_public;this.province_id=province_id;
        this.city_id=city_id;this.has_image=has_image;this.active=active;this.created_at=created_at;this.updated_at=updated_at;this.img1=img1;this.img2=img2;this.img3=img3;this.img4=img4;

    }


    public Advertising(String id,String company_id,String category_id,String name,String visit,String hoze,String company_name,String description,String products,String conditions,String email,String phone,String
            email_public ,String phone_public,String province_id,String city_id,String has_image,String active,String created_at,String updated_at,String img1,String img2,String img3,String img4,String address,String why){

        this.id=id;
        this.company_id=company_id;
        this.category_id=category_id;
        this.name=name;this.visit=visit;
        this.address = address;
        this.hoze = hoze;
        this.why = why;
        this.company_name=company_name;this.description=description;this.products=products;
        this.conditions=conditions;this.email=email;this.phone=phone;this.email_public =email_public;this.phone_public=phone_public;this.province_id=province_id;
        this.city_id=city_id;this.has_image=has_image;this.active=active;this.created_at=created_at;this.updated_at=updated_at;this.img1=img1;this.img2=img2;this.img3=img3;this.img4=img4;

    }

}
