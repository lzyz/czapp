package com.czapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contactperson extends BaseActivity {

    private FloatingActionButton add;

    MyAdapter adapter;
    //ArrayAdapter<String> adapter;

    Map<String,String> saved = new HashMap<String, String>();//从文件中读取的已添加的紧急联系人
    Map<String,String> tlist = new HashMap<String, String>();//从通讯录中读取的还未添加的联系人
    Map<String,String> checked = new HashMap<String, String>();//选中的要添加的联系人
    private boolean isMulChoice = false; //是否多选
    private List<String> selectid = new ArrayList<String>();
    ListView contactlist;
    List<String> contactsList = new ArrayList<>();
    public int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactperson);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        contactlist = (ListView) findViewById(R.id.list);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setupBackAsUp("设置联系人");
        readFile();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_CONTACTS }, 1);
        }
        add = (FloatingActionButton) findViewById(R.id.addFloatingActionButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(Contactperson.this);
                builder.setTitle("选择联系人");
                readContacts();
                builder.setMultiChoiceItems(toStrings(tlist), null, new DialogInterface.OnMultiChoiceClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked)
                    {
                        String[] temp = toStrings(tlist)[which].split(" ");
                        if(isChecked)
                        {
                            checked.put(temp[0],temp[1]);
                        }
                        else
                        {
                            checked.remove(temp[0]);
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        for(Map.Entry<String,String> c:checked.entrySet())
                        {
                            saved.put(c.getKey(),c.getValue());
                        }
                        if(checked.size()>0)
                        {
                            saveFile();
                            readFile();
                        }
                        checked.clear();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(flag==1)
        {
            Log.d("visible:","yes");
            menu.findItem(R.id.delete).setVisible(false);
            menu.findItem(R.id.cancel).setVisible(true);
            menu.findItem(R.id.yes).setVisible(true);
        }
        else
        {
            Log.d("visible:","delete");
            menu.findItem(R.id.delete).setVisible(true);
            menu.findItem(R.id.cancel).setVisible(false);
            menu.findItem(R.id.yes).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.yes:
                flag = 0;
                invalidateOptionsMenu();
                isMulChoice = false;
                for(String t:selectid)
                {
                     if(saved.containsKey(t.split(" ")[0]))
                     {
                         Log.d("删除",saved.get(t.split(" ")[0]));
                         saved.remove(t.split(" ")[0]);
                     }
                }
                selectid.clear();
                saveFile();
                readFile();
                isMulChoice = false;
                adapter = new MyAdapter(this);
                contactlist.setAdapter(adapter);
                add.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel:
                flag = 0;
                invalidateOptionsMenu();
                selectid.clear();
                isMulChoice = false;
                adapter = new MyAdapter(this);
                contactlist.setAdapter(adapter);
                add.setVisibility(View.VISIBLE);
                break;
            case R.id.delete:
                flag = 1;
                invalidateOptionsMenu();
                isMulChoice = true;
                selectid.clear();
                add.setVisibility(View.GONE);
                adapter = new MyAdapter(this);
                contactlist.setAdapter(adapter);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);

        return true;
    }

    private void readFile()
    {
        contactsList.clear();
        FileInputStream in = null;
        BufferedReader reader = null;
        try {
            in = openFileInput("contactperson");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] temp = line.split(" ");
                saved.put(temp[0],temp[1]);
            }
            if(saved.size()>0)
            {
                for(Map.Entry<String,String> a:saved.entrySet())
                {
                    contactsList.add(a.getKey()+" "+a.getValue());
                }
                //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactsList);
                adapter = new MyAdapter(this);
                contactlist.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveFile()
    {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("contactperson", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            for(Map.Entry<String,String> c:saved.entrySet())
            {
                writer.write(c.getKey()+" "+c.getValue());
                writer.newLine();
            }
            saved.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] toStrings(Map<String,String> keys)
    {
        String[] key = new String[keys.size()];
        int i=0;
        for(Map.Entry k:keys.entrySet())
        {
            key[i++] = k.getKey()+" "+k.getValue();
        }
        return key;
    }

    private void readContacts() {
        Cursor cursor = null;
        tlist.clear();
        try {
            // 查询联系人数据
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    // 获取联系人手机号
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if(saved.containsKey(displayName))
                    {
                    }
                    else
                    {
                        tlist.put(displayName,number.replaceAll(" ",""));
                    }
                }
            }
            else {
                Toast.makeText(this, "您的通讯录没有联系人", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater=null;
        private HashMap<Integer, View> mView ;
        public  HashMap<Integer, Integer> visiblecheck ;//用来记录是否显示checkBox
        public  HashMap<Integer, Boolean> ischeck;

        public MyAdapter(Context context)
        {
            this.context = context;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = new HashMap<Integer, View>();
            visiblecheck = new HashMap<Integer, Integer>();
            ischeck      = new HashMap<Integer, Boolean>();
            if(isMulChoice){
                for(int i=0;i < contactsList.size();i++){
                    ischeck.put(i, false);
                    visiblecheck.put(i, CheckBox.VISIBLE);
                }
            }else{
                for(int i=0;i<contactsList.size();i++)
                {
                    ischeck.put(i, false);
                    visiblecheck.put(i, CheckBox.INVISIBLE);
                }
            }
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return contactsList.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return contactsList.get(position);
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @SuppressWarnings("WrongConstant")
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = mView.get(position);
            if(view==null)
            {
                view = inflater.inflate(R.layout.item, null);
                TextView txt = (TextView)view.findViewById(R.id.txtName);
                final CheckBox ceb = (CheckBox)view.findViewById(R.id.check);

                txt.setText(contactsList.get(position));

                ceb.setChecked(ischeck.get(position));
                ceb.setVisibility(visiblecheck.get(position));

                view.setOnLongClickListener(new Onlongclick());

                view.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(isMulChoice){
                            if(ceb.isChecked()){
                                ceb.setChecked(false);
                                selectid.remove(contactsList.get(position));
                            }else{
                                ceb.setChecked(true);
                                selectid.add(contactsList.get(position));
                            }
                        }
                    }
                });

                mView.put(position, view);
            }
            return view;
        }

        class Onlongclick implements View.OnLongClickListener {

            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                isMulChoice = true;
                selectid.clear();
                /*for(int i=0;i < contactsList.size();i++)
                {
                    adapter.visiblecheck.put(i, CheckBox.VISIBLE);
                }*/
                flag = 1;
                add.setVisibility(View.GONE);
                invalidateOptionsMenu();
                adapter = new MyAdapter(context);
                contactlist.setAdapter(adapter);
                return true;
            }
        }
    }
}
