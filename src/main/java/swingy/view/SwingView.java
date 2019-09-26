package swingy.view;

import swingy.Board;
import swingy.Hero;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingView implements GameView
{

    private JTextField messageInput;
    private JTextPane boardDisplay;
    private JTextArea heroDisplay;
    private JTextArea messageArea;
    private JScrollPane messageAreaPane;
    private JScrollPane boardDisplayPane;
    private JScrollPane heroDisplayPane;

    private JFrame frame;

    private String inputMessage = null;


    public SwingView()
    {
    }

    @Override
    public void init()
    {
        frame = new JFrame("Swingy");
        frame.setResizable(false);
        frame.setLocation(400, 200);

        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.ipady = 400;
        c.weightx = .1;
        heroDisplay = new JTextArea();
        heroDisplay.setEditable(false);
        heroDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        heroDisplayPane = new JScrollPane(heroDisplay);
        frame.add(heroDisplayPane, c);

        c.gridx = 1;
        boardDisplay = new JTextPane();
        boardDisplay.setEditable(false);
        boardDisplay.setFont(new Font("monospaced", Font.PLAIN, 12));
        boardDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        boardDisplayPane = new JScrollPane(boardDisplay);
        StyledDocument doc = boardDisplay.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        frame.add(boardDisplayPane, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        messageAreaPane = new JScrollPane(messageArea);
        messageAreaPane.setAutoscrolls(true);
        frame.add(messageAreaPane, c);

        c.gridy = 2;
        c.ipady = 0;
        messageInput = new JTextField();
        messageInput.setPreferredSize(new Dimension(800, 30));
        messageInput.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                inputMessage = messageInput.getText();
                messageInput.setText("");
            }
        });
        frame.add(messageInput, c);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }

    @Override
    public String promptUser(String prompt)
    {
        messageUser(prompt);
        while (inputMessage == null) {
            try {
                // We sleep to allow the cpu to 'breathe', won't return promptly otherwise.
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String tmp = inputMessage;
        inputMessage = null;
        return tmp;
    }

    @Override
    public void messageUser(String msg)
    {
        messageArea.append(msg + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    @Override
    public void updateHeroData(Hero h)
    {
        heroDisplay.setText(String.format(
                "You are %s the level %d %s with a total health of %d\n" +
                        "On your head you wield a %s with a health of %d\n" +
                        "On your person you wield a %s with an armor of %d\n" +
                        "In your hands you wield a %s with a power of %d\n",
                h.name, h.level(), h.profession, h.health(),
                h.helm.name, h.helm.health,
                h.armor.name, h.armor.armor,
                h.weapon.name, h.weapon.power
        ));
    }

    @Override
    public void updateBoardData(Board b, Hero h)
    {
        StringBuilder builder = new StringBuilder();
        Point p = new Point();

        for (int j = 0; j < b.size; j++) {
            for (int i = 0; i < b.size; i++) {
                p.x = i;
                p.y = j;
                if (h.position.equals(p))
                    builder.append("X");
                else if (b.visited(p))
                    builder.append("-");
                else
                    builder.append("?");
            }
            builder.append("\n");
        }
        boardDisplay.setText(builder.toString());
    }

    @Override
    public void clearScreen()
    {
        messageArea.setText("");
    }

    @Override
    public void lock()
    {
        messageInput.setEditable(false);
    }

    @Override
    public void hide()
    {
        frame.dispose();
    }

    @Override
    public void show()
    {
        frame.setVisible(true);
        frame.setEnabled(true);
    }
}