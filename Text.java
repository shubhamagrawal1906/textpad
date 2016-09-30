import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;
public final class Text extends JFrame implements ActionListener{
    
    protected UndoManager undoManager = new UndoManager();

    int caretpos;
    String findstr="";
    JLabel label;
    JMenuItem knew,open,save,save_as,exit;
    JMenuItem undo,redo,cut,copy,paste,delete,find,selectAll;
    JMenuItem font,about;
    JCheckBoxMenuItem wordwrap,statusbar,linebar;
    JMenu file,edit,format,view,help;
    JTextField t;
    JTextArea text,lines;
    String filepath = "C:\\Users\\SHUBHAM\\Downloads\\Untitled.txt";
    File f = new File(filepath);
    boolean countSave=true,confirmSave=true,countBar=true,countStatus;
    
    Text(){
        
        knew = new JMenuItem("New");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        save_as = new JMenuItem("Save As");
        exit = new  JMenuItem("Exit");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");
        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        delete = new JMenuItem("Delete");
        find = new JMenuItem("Find");
        selectAll = new JMenuItem("Select All");
        wordwrap = new JCheckBoxMenuItem("Word Wrap");
        font = new JMenuItem("Font");    
        statusbar = new JCheckBoxMenuItem("Status Bar");
        linebar = new JCheckBoxMenuItem("Line Bar");
        about = new JMenuItem("About");
        
        file = new JMenu("File");
        edit = new JMenu("Edit");
        format = new JMenu("Format");
        view = new JMenu("View");
        help = new JMenu("Help");
         
        file.add(knew);
        file.add(open);
        file.add(save);
        file.add(save_as);
        file.add(exit);
        
        edit.add(undo);
        edit.add(redo);
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        edit.add(find);
        edit.add(selectAll);
        
        format.add(wordwrap);
        format.add(font);
        
        view.add(statusbar);
        view.add(linebar);
        
        help.add(about);
        
        JMenuBar jmb =new JMenuBar();
        add(jmb,BorderLayout.NORTH);
        jmb.add(file);
        jmb.add(edit);
        jmb.add(format);
        jmb.add(view);
        jmb.add(help);
        
        text=new JTextArea();
        text.addCaretListener(new CaretListener(){
            @Override
            public void caretUpdate(CaretEvent e){
                JTextArea t = (JTextArea)e.getSource();
                int line=1,col=1;
                
                try {
                    caretpos = t.getCaretPosition();
                    line = t.getLineOfOffset(caretpos);
                    col = caretpos - t.getLineStartOffset(line);
                    line++;
                    col++;
                } catch (Exception ex) { }
                
                updateStatus(line,col);
            }
            
        });
        
        lines = new JTextArea("1       "); 
        lines.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.DARK_GRAY));
        lines.setEditable(false);
        lines.setBackground(Color.LIGHT_GRAY);
        text.getDocument().addDocumentListener(new DocumentListener(){
            
            public String getText(){
                int pos=text.getDocument().getLength();
                Element root=text.getDocument().getDefaultRootElement();
                String t = "1       " + System.getProperty("line.separator");
                
                for(int i=2;i<root.getElementIndex(pos)+2;i++)
                    t+= i + System.getProperty("line.separator");
                return t;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                lines.setText(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lines.setText(getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lines.setText(getText());
            }
        });
        
        text.getDocument().addUndoableEditListener(new UndoableEditListener(){

            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
            undoManager.addEdit(e.getEdit());
            updateUndoRedo();
            }    
        });
        
        JPanel panel = new JPanel();
        label =new JLabel();
        panel.setPreferredSize(new Dimension(getWidth(),20));
        panel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(label);
        updateStatus(1,1);
        add(panel,BorderLayout.SOUTH);
        panel.setVisible(false);
        
        JScrollPane scroll = new JScrollPane(text);
        scroll.setRowHeaderView(lines);
        scroll.setRowHeaderView(lines);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        
        wordwrap.setSelected(false);
        statusbar.setSelected(false);
        linebar.setSelected(true);
        
        knew.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);
        save_as.addActionListener(this);
        exit.addActionListener(this);
        undo.addActionListener(this);
        redo.addActionListener(this);
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        delete.addActionListener(this);
        find.addActionListener(this);
        selectAll.addActionListener(this);
        wordwrap.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton button = (AbstractButton) e.getSource();
            if(button.isSelected()){
                text.setLineWrap(true);
                panel.setVisible(false);
                countBar=false;
                scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            }else{
                text.setLineWrap(false);
                countBar=true;
                panel.setVisible(countStatus);
                scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            }    
        }
    });
        font.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                int i;    
            }    
        });
        statusbar.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            AbstractButton button = (AbstractButton) e.getSource();
            if(countBar){
                if(button.isSelected()){
                    countStatus=true;
                }else{
                    countStatus=false;
                }
                panel.setVisible(countStatus);
            }    
        }
    });
        linebar.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton button = (AbstractButton) e.getSource();
            if(button.isSelected()){
                lines.setVisible(true);
            }else{
                lines.setVisible(false);
            }
        }    
    });
        Title();
        add(scroll,BorderLayout.CENTER);
        setSize(637,385);
        setVisible(true);   
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==knew){
            newFile();
        }
        else if(e.getSource()==open){
            openFile();
        }
        else if(e.getSource()==save){
            saveFile();
        }
        else if(e.getSource()==save_as){
            saveAsFile();
        }
        else if(e.getSource()==exit){
            exitFile();
        }
        if(e.getSource()==undo){
            undoFile();
        }
        else if(e.getSource()==redo){
            redoFile();
        }
        else if(e.getSource()==cut){
            text.cut();
        }
        else if(e.getSource()==copy){
            text.copy();
        }
        else if(e.getSource()==paste){
            text.paste();
        }
        else if(e.getSource()==delete){
            text.replaceSelection("");
        }
        else if(e.getSource()==find){
            findFile();
        }
        else if(e.getSource()==selectAll){
            text.selectAll();
        }
    }
    
    void Title(){
        String str;
        str=f.getName();
        setTitle(str+" - Notepad");
    }
    
    void newFile(){
        
        File newfile = new File(filepath);
        
        if(newfile.exists()){
            if(compare() && confirmSave){}
            else{
                //create panel with save, don't save and cancel button.Use save.
                if(!checkConfirmBox("SAVE")){return;}
            }
        }
        else{
            //create panel with save, don't save and cancel button.Use saveas. 
            if(!checkConfirmBox("SAVE_AS")){return;}
            }
        confirmSave=false;
        newContent();
    }
    
    void newContent(){
        String s1="";
        text.setText(s1);
        countSave=true;
        filepath = "C:\\Users\\SHUBHAM\\Downloads\\Untitled.txt";
        f = new File(filepath);
        Title();
    }
    
    void openFile(){
        
        int i;
        JFileChooser fc = new JFileChooser();
        do{
            i = fc.showOpenDialog(open);
            if(i!=JFileChooser.APPROVE_OPTION)
                return;
            f = fc.getSelectedFile();
            filepath = f.getPath();
            if(f.exists())    
                break;
            JOptionPane.showMessageDialog(null, "File not found.\nPlease, verify the current filename was given.", "Notepad", JOptionPane.INFORMATION_MESSAGE);
        }while(true);
        openContent();
    }
    
    
    void openContent(){
        
        String s1="",s2="";
        
        try{
            
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            
            s1=br.readLine();
            while(s1!=null){
                s2+=s1+"\n";
                s1=br.readLine();
            }
            
            text.setText(s2);
            
        }catch(Exception e){
            System.out.print("Not able to open the data");
        }
    }
    
    void saveFile(){
        if(countSave){
            JFileChooser fc = new JFileChooser();
            int i=fc.showSaveDialog(save);
            if(i==JFileChooser.APPROVE_OPTION){
                f = fc.getSelectedFile();
                if(f.exists()){
                    Object[] options={"Yes","No"};
                int response=JOptionPane.showOptionDialog(null,
                        "Do you want to replace the existing file?",
                        "Confirm Save As",JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,null,
                        options,"No");
                    if(response!=0){
                    return;
                }
            }
                           
                filepath = f.getPath();
                saveContent();
                countSave=false;
                Title();
            }
        }else{
            saveContent();
        }
        
    }
    
    void saveAsFile(){
        JFileChooser fc;
        fc = new JFileChooser();
        int i=fc.showSaveDialog(save);
        if(i==JFileChooser.APPROVE_OPTION){
            f = fc.getSelectedFile();
            if(f.exists()){
                Object[] options={"Yes","No"};
                int response=JOptionPane.showOptionDialog(null,
                        "Do you want to replace the existing file?",
                        "Confirm Save As",JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,null,
                        options,"No");
                if(response!=0){
                    return;
                }
            }
            
            filepath = f.getPath();
            saveContent();
            countSave=false;
            Title();
        }        
    }
    
    void saveContent(){
        int i;
        String[] lines= text.getText().split("\n");
        
            try{
                
                PrintWriter writer = new PrintWriter(filepath);
                
                writer.write("");
                writer.close();
                
                BufferedWriter bw = new BufferedWriter(new FileWriter(filepath,true));
                
                for(i=0;i<lines.length;i++){
                
                    bw.write(lines[i]);
                    bw.newLine();
                    bw.flush();
                }
                
                bw.close();
                
            }catch(Exception e){
                System.out.println("Not able to save the data");
            }
        
    }

    void exitFile(){
        
        File exitfile = new File(filepath);
        
        if(exitfile.exists()){
            if(compare() && confirmSave){}
            else{
                //create panel with save, don't save and cancel button.Use save.
                if(!checkConfirmBox("SAVE")){return;}
            }
        }
        else{
            //create panel with save, don't save and cancel button.Use saveas. 
            if(!checkConfirmBox("SAVE_AS")){return;}
            }
        System.exit(0);
    }
    
    boolean compare(){
        String[] line;
        String s1;
        int i=0;
        line = text.getText().split("\n");
        try{
            
            BufferedReader bw = new BufferedReader(new FileReader(filepath));
            
            s1=bw.readLine();
            while(s1!=null){
                if(!(s1.equals(line[i]))){
                    return false;
                }
                i++;
                s1=bw.readLine();
            }
            if(i!=line.length){
                return false;
            }
        }catch(Exception e){
        }
        
        return true;
    }
    
    boolean checkConfirmBox(String s){
        Object[] options={"Save","Don't Save","Cancel"};
        int jop = JOptionPane.showOptionDialog(null,
        "Do you want to save the file?",
        "Notepad",JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,null,
        options,"Save");
            if(jop==0){
                if(s=="SAVE"){saveFile();}
                else if(s=="SAVE_AS"){saveAsFile();}
            }else if(jop==1){
            }else if(jop==3){
                return false;
            }

        return true;
    }
    
    public void undoFile(){
        try{
            undoManager.undo();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Cannot able to undo.", "Notepad", JOptionPane.INFORMATION_MESSAGE);
        }
        updateUndoRedo();
    }
    
    public void redoFile(){
        try{
            undoManager.redo();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Cannot able to redo.", "Notepad", JOptionPane.INFORMATION_MESSAGE);
        }
        updateUndoRedo();
    }
    public void updateUndoRedo(){
        undo.setEnabled(undoManager.canUndo());
        redo.setEnabled(undoManager.canRedo());
    }
    public void updateStatus(int linenum,int columnum){
        label.setText("Line : "+linenum+"  Col : "+columnum);
    }
    
    private JPanel p1(){
        JPanel bp1 = new JPanel();
        bp1.setVisible(true);
        bp1.setOpaque(true);
        bp1.setBackground(Color.BLACK);
        JPanel cp = new JPanel();
        cp.setBackground(Color.WHITE);
        cp.setLayout(new GridLayout(2,2,5,5));
        cp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cp.setOpaque(true);
        JLabel l = new JLabel("Find what :");
        t = new JTextField();
        JPanel sp1 = new JPanel();
        JLabel l1 = new JLabel("                                                     ");
        JLabel l2 = new JLabel("                                                     ");
        sp1.setBackground(Color.WHITE);
        sp1.setLayout(new BorderLayout());
        sp1.add(BorderLayout.NORTH, l1);
        sp1.add(BorderLayout.CENTER, t);
        sp1.add(BorderLayout.SOUTH, l2);
        JCheckBox c = new JCheckBox("Match case");
        c.setBackground(Color.WHITE);
        JPanel sp2 = new JPanel();
        sp2.setBackground(Color.WHITE);
        sp2.setBorder(BorderFactory.createTitledBorder("Direction"));
        JRadioButton rb1 = new JRadioButton("Up");
        JRadioButton rb2 = new JRadioButton("Down");
        rb1.setBackground(Color.WHITE);
        rb2.setBackground(Color.WHITE);
        rb2.setSelected(true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rb1);
        bg.add(rb2);
        cp.add(l);
        cp.add(sp1);
        cp.add(c);
        sp2.add(rb1);
        sp2.add(rb2);
        cp.add(sp2);        
        bp1.add(cp);
        return bp1;
    }
    void findFile(){
        boolean b=true;
        do{
            Object[] options={"Find Next","Cancel"};
            int res = JOptionPane.showOptionDialog(null, p1(), "Find"
                    , JOptionPane.DEFAULT_OPTION
                    , JOptionPane.PLAIN_MESSAGE
                    , null, options, "Find Next");
            int i = caretpos;
            if(!findstr.equals(""))
                t.setText(findstr);
            if(res==0){
                down();
                b=true;
            }
            else if(res==1){
                b=false;
            }

        }while(b);
        
    }
    void down(){
        int i=caretpos,j,k=0;
        String str = text.getText();
        findstr = t.getText();
        j = str.indexOf(findstr, i);
        caretpos+=findstr.length();
        System.out.println(j);
    }
    
    public static void main(String args[]){
        Text obj=new Text();
    }
   
}
