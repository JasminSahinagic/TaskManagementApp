package com.example.taskmanagementapp.taskmanagementapp.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.taskmanagementapp.taskmanagementapp.Model.Task;
import com.example.taskmanagementapp.taskmanagementapp.R;
import com.example.taskmanagementapp.taskmanagementapp.TaskDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserTasksAdapter extends RecyclerView.Adapter<UserTasksAdapter.ViewHolder> {

    Context context;
    List<Task> taskList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;


    public UserTasksAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public UserTasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row,parent,false);
        return new ViewHolder(view,context);


    }

    @Override
    public void onBindViewHolder(@NonNull UserTasksAdapter.ViewHolder holder, int position) {

        Task task = taskList.get(position);
        String imageUrl = null;
        holder.textViewRowTitle.setText(task.getTitle());
        holder.textViewRowDate.setText(task.getDateTaskAdded());
        imageUrl = task.getImage();
        Picasso.with(context).load(imageUrl).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public TextView textViewRowTitle;
        public TextView textViewRowDate;
        public Button buttonCkeck;
        public  Button buttonDelete;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewRowImage);
            textViewRowTitle = (TextView) itemView.findViewById(R.id.textViewRowTitle);
            textViewRowDate = (TextView) itemView.findViewById(R.id.textViewRowDate);
            buttonCkeck=(Button) itemView.findViewById(R.id.buttonCheck);
            buttonDelete=(Button) itemView.findViewById(R.id.buttonDelete);

            buttonCkeck.setOnClickListener(this);
            buttonDelete.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int temp = getAdapterPosition();
                    Task task = taskList.get(temp);
                    Intent intent = new Intent(context, TaskDetails.class);
                    intent.putExtra("Title",task.getTitle());
                    intent.putExtra("Description",task.getDescription());
                    intent.putExtra("Date",task.getDateTaskAdded());
                    intent.putExtra("Image",task.getImage());
                    taskList.clear();
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.buttonCheck:
                    checkTask();

                    break;
                case R.id.buttonDelete:
                    deleteTask();
                    break;
            }
        }

        private void checkTask() {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view  = inflater.inflate(R.layout.alert_dialog,null);

            Button buttonAlertNo = (Button) view.findViewById(R.id.buttonAlertNo);
            Button buttonAlertYes = (Button) view.findViewById(R.id.buttonAlertYes);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            buttonAlertNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                }
            });
            buttonAlertYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cardView.setCardBackgroundColor(Color.GREEN);
                    buttonCkeck.setBackgroundResource(R.drawable.round_check_circle_black_18dp);
                    dialog.dismiss();

                }
            });

        }

        private void deleteTask() {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view  = inflater.inflate(R.layout.alert_dialog,null);

            Button buttonAlertNo = (Button) view.findViewById(R.id.buttonAlertNo);
            Button buttonAlertYes = (Button) view.findViewById(R.id.buttonAlertYes);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            buttonAlertNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAlertYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int num = getAdapterPosition();
                    Task task = taskList.get(num);
                    String key = task.getRemoveID();
                    Log.d("TaskKEy",key);

                    database = FirebaseDatabase.getInstance();
                    mDatabase = database.getReference().child("Tasks");
                    mDatabase.child(key).removeValue();
                    notifyItemRemoved(getAdapterPosition());
                    taskList.remove(num);
                    dialog.dismiss();

                }
            });



        }
    }
}
