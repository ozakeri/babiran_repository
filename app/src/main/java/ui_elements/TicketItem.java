package ui_elements;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.babiran.app.R;


public class TicketItem extends RelativeLayout {


    public Context contex;

    public TextView body;
    public int question = 1;
    //public TextView description;







    public TicketItem(Context context, String body,int question) {
        super(context);

        init(context, body,question);
    }



    public void init(final Context context, final String body,int question ) {
        contex=context;
        if(question == 1) {
            inflate(getContext(), R.layout.ticket_item_me, this);
        }else if(question == 0) {
            inflate(getContext(), R.layout.ticket_item_you, this);
        }


        this.body = (TextView)findViewById(R.id.body);
        this.body.setText(body);
        //this.description = (TextView)findViewById(R.id.card_description);



    }


}