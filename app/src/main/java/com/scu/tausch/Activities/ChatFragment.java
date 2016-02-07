package com.scu.tausch.Activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.scu.tausch.Adapters.ChatListAdapter;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.DTO.Message;
import com.scu.tausch.R;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    static final String TAG = ChatFragment.class.getSimpleName();
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    static String RECEIVER_ID_KEY = "receiverId";
static String dealObj="";

    private EditText etMessage;
    private Button btSend;
    private View rootView;

    ListView lvChat;
    ArrayList<Message> mMessages;
    ChatListAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;

   // static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
   // static final int POLL_INTERVAL = 100; // milliseconds
    // Create a handler which can run code periodically
//    final Handler mHandler = new Handler();  // android.os.Handler
//    Runnable mRefreshMessagesRunnable = new Runnable() {
//        @Override
//        public void run() {
//         //   refreshMessages();
//       //     mHandler.postDelayed(this, POLL_INTERVAL);
//        }
//    };

    public ChatFragment(){
     // Required empty public constructor

   }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   mHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        ParseUser myCurrentUser = ParseUser.getCurrentUser();

        // User login
        if (myCurrentUser != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            // login();
            //We need current user many times, so need to make sure its not null.
          //  if (ParseUser.getCurrentUser()==null) {
//                ParseUser.enableAutomaticUser();
//                ParseUser.getCurrentUser().saveInBackground();
//            startWithCurrentUser();
         //   }
        }

        return rootView;
    }

    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        setupMessagePosting();
    }


    // Setup message field and posting
    void setupMessagePosting() {
        // Find the text field and button



        etMessage = (EditText) rootView.findViewById(R.id.etMessage);
        btSend = (Button) rootView.findViewById(R.id.btSend);
        lvChat = (ListView) rootView.findViewById(R.id.lvChat);
        mMessages = new ArrayList<>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new ChatListAdapter(getActivity(), userId, mMessages);
        lvChat.setAdapter(mAdapter);
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.whereEqualTo("username", "Adhuri@scu.edu");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            // row of Object Id "U8mCwTHOaC"

                            for (final ParseObject dealsObject : objects) {
                                // use dealsObject.get('columnName') to access the properties of the Deals object.
                                dealObj = dealsObject.getObjectId();

                                String data = etMessage.getText().toString();
                                ParseObject message = ParseObject.create("Message");
                              //  message.put(Message.USER_ID_KEY, userId);

                                //object id for Adhuri@scu.edu
                                message.put(Message.USER_ID_KEY, "xX9N1EXVa9");

                                message.put(Message.BODY_KEY, data);

                                //currently its for pjain3@scu.edu, it should be receiver dealObj
                                message.put(Message.RECEIVER_ID_KEY, "Q1HVhZTKHe");
                                message.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        Toast.makeText(getActivity(), "Successfully created message on Parse",
                                                Toast.LENGTH_SHORT).show();


//                                        ParseQuery pushQuery = ParseInstallation.getQuery();
//                                        pushQuery.whereEqualTo(Message.RECEIVER_ID_KEY, dealObj);
//                                     //   ParseUser currentUser = ParseUser.getCurrentUser();
//                                       // String message = currentUser.getString("name") + " says Hi!";
//
//                                        ParsePush push = new ParsePush();
//                                        push.setQuery(pushQuery); // Set our Installation query
//                                        push.setMessage("data");
//                                        push.sendInBackground();







//                                        ParsePush push = new ParsePush();
//                                        String message = "Client message testinggg";
//                                        push.setChannel("abc");
//                                        push.setMessage(message);
//                                        push.sendInBackground();


                                        ParsePush parsePush = new ParsePush();
                                        ParseQuery pQuery = ParseInstallation.getQuery(); // <-- Installation query
                                        pQuery.whereEqualTo("username", "praneetchhabra@gmail.com"); // <-- you'll probably want to target someone that's not the current user, so modify accordingly
                                        parsePush.sendMessageInBackground("Only for special people", pQuery);





                                        //   refreshMessages();
                                    }
                                });
                                etMessage.setText(null);

                            }
                        } else {
                            // error
                        }
                    }
                });




            }
        });
    }

    // Query messages from Parse so we can load them into the chat adapter
//    void refreshMessages() {
//
//        // Construct query to execute
//        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
//        // Configure limit and sort order
//        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
//        query.orderByAscending("createdAt");
//        // Execute query to fetch all messages from Parse asynchronously
//        // This is equivalent to a SELECT query with SQL
//
//        query.findInBackground(new FindCallback<Message>() {
//            @Override
//            public void done(List<Message> messages, com.parse.ParseException e) {
//                if (e == null) {
//                    mMessages.clear();
//                    mMessages.addAll(messages);
//                    mAdapter.notifyDataSetChanged(); // update adapter
//                    // Scroll to the bottom of the list on initial load
//                    if (mFirstLoad) {
//                        lvChat.setSelection(mAdapter.getCount() - 1);
//                        mFirstLoad = false;
//                    }
//                } else {
//                    Log.e("message", "Error Loading Messages" + e);
//                }
//            }
//        });
//
//    }


    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
//        ParseAnonymousUtils.logIn(new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Anonymous login failed: ", e);
//                } else {
//                    startWithCurrentUser();
//                }
//            }
//        });

//        ParseUser.logInInBackground("pjain3@scu.edu", "111", new LogInCallback() {
//            public void done(ParseUser user, ParseException e) {
//                if (user != null) {
//                    // Hooray! The user is logged in.
//                } else {
//                    // Signup failed. Look at the ParseException to see what happened.
//                }
//            }
//        });

    }

}