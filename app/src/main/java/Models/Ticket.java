package Models;

/**
 * Created by Tohid on 2/9/2017 AD.
 */

public class Ticket {

    public String id,company_id,company_name,email,body,question,created_at,active,updated_at;
    public Ticket(String id,String company_id,String company_name,String email,String body,String question,String created_at,String active,String updated_at){
        this.id = id;
        this.company_id = company_id;
        this.company_name = company_name;
        this.email=email;
        this.body = body;
        this.question = question;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.active = active;
    }
}
