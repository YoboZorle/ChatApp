package beaststudio.in.chatapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by beast on 14/4/17.
 */

public class chat_rec extends RecyclerView.ViewHolder  {

    TextView text, user,time;

    public chat_rec (View itemView){
        super(itemView);

        text = (TextView)itemView.findViewById(R.id.message_text);
        user = (TextView)itemView.findViewById(R.id.message_user);
        time = (TextView)itemView.findViewById(R.id.message_time);

    }
}
