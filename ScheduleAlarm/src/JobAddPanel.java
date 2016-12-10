import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class JobAddPanel extends JPanel{
	JPanel currentJobPanel = new JPanel();
	JTextPane currentJobText = new JTextPane();
	JobInfo currentJob;
	public JobAddPanel(){
		JTextField jobNameField = new JTextField("Input Job Name");
		JButton addButton = new JButton("Add Job");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addJob(jobNameField.getText());
			}
		});
		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateJob();
			}
		});
		this.setLayout(new BorderLayout());
		this.add(jobNameField, BorderLayout.NORTH);
		this.add(addButton, BorderLayout.CENTER);
		this.add(updateButton, BorderLayout.EAST);
		this.currentJobPanel.add(this.currentJobText);
		this.add(this.currentJobPanel, BorderLayout.SOUTH);
	}
	
	public void addJob(String name){
		this.currentJob = new JobInfo(name);
		updateJob();
	}
	
	public void updateJob(){
		this.remove(this.currentJobPanel);
		this.currentJobPanel.removeAll();
		this.currentJobText.setText(this.currentJob.toString());
		this.currentJobPanel.add(this.currentJobText);
		this.add(this.currentJobPanel, BorderLayout.SOUTH);
		this.revalidate();
		this.repaint();
	}
	
}
