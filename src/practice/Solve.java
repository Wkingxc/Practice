package practice;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import static java.lang.Math.*;

public class Solve extends JFrame implements ActionListener {
    double[] t = new double[]{0.0, 0.0, 1.84, 1.32, 1.2, 1.14, 1.11, 1.09, 1.08, 1.07, 1.06, 1.05, 1.03};
    double[] x = new double[20];
    double[] y = new double[20];
    int xcount;
    int ycount;
    JTextField tfx = new JTextField();
    JTextField tfy = new JTextField();
    JTextArea tf3 = new JTextArea();
    JLabel label3 = new JLabel("cj.Wang");
    JLabel label1 = new JLabel("主要输入框(x)");
    JLabel label2 = new JLabel("可选输入框(y)");
    JButton button0 = new JButton("录入数据");
    JButton button1 = new JButton("不确定度");
    JButton button2 = new JButton("检测1次坏值");
    JButton button3 = new JButton("线性相关系数");
    JButton button4 = new JButton("线性回归方程");
    JButton button5 = new JButton("清空数据");

    public Solve() {
    }

    void go() {
        JFrame frame = new JFrame("物理实验数据处理");
        frame.setLayout(null);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        label1.setBounds(30, 0, 300, 30);
        label2.setBounds(30, 90, 300, 30);
        label3.setBounds(430, 420, 120, 60);
        button0.setBounds(30, 410, 100, 50);
        button1.setBounds(350, 30, 120, 30);
        button2.setBounds(350, 80, 120, 30);
        button3.setBounds(350, 130, 120, 30);
        button4.setBounds(350, 180, 120, 30);
        button5.setBounds(200, 410, 100, 50);
        button0.addActionListener(this);
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
        tfx.setBounds(30, 30, 300, 50);
        tfy.setBounds(30, 120, 300, 50);
        tf3.setBounds(30, 200, 300, 200);
        tf3.setEditable(false);
        tf3.setFont(new Font("Dialog",Font.BOLD,20));
        frame.getContentPane().add(button0);
        frame.getContentPane().add(button1);
        frame.getContentPane().add(button2);
        frame.getContentPane().add(button3);
        frame.getContentPane().add(button4);
        frame.getContentPane().add(button5);
        frame.getContentPane().add(label1);
        frame.getContentPane().add(label2);
        frame.getContentPane().add(label3);
        frame.getContentPane().add(tf3);
        frame.getContentPane().add(tfx);
        frame.getContentPane().add(tfy);
        frame.setVisible(true);
    }

    double solve_uncertainty(double[] data, int cnt) {
        double s = solve_Bessle(data, cnt);
        return t[cnt] * s / sqrt(cnt);
    }

    void getXData(String s) {
        StringBuilder temp = new StringBuilder();
        int len = s.length();

        for(int i = 0; i < len; ++i) {
            if (s.charAt(i) == ' ') {
                x[xcount++] = Double.parseDouble(temp.toString());
                temp = new StringBuilder();
            } else if (i == len - 1) {
                temp.append(s.charAt(i));
                x[xcount++] = Double.parseDouble(temp.toString());
            } else {
                temp.append(s.charAt(i));
            }
        }

    }
    void getYData(String s) {
        StringBuilder temp = new StringBuilder();
        int len = s.length();
        for(int i = 0; i < len; ++i) {
            if (s.charAt(i) == ' ') {
                y[ycount++] = Double.parseDouble(temp.toString());
                temp = new StringBuilder();
            } else if (i == len - 1) {
                temp.append(s.charAt(i));
                y[ycount++] = Double.parseDouble(temp.toString());
            } else {
                temp.append(s.charAt(i));
            }
        }
    }
    double average(double[] data, int cnt) {
        double sum = 0.0;
        for(int i = 0; i < cnt; ++i) {
            sum += data[i];
        }
        return sum / (double)cnt;
    }
    double solve_Bessle(double[] data, int cnt) {
        double avrg = average(data, cnt);
        double sum = 0.0D;

        for(int i = 0; i < cnt; ++i) {
            sum += pow(data[i] - avrg, 2.0D);
        }

        return sqrt(sum / (double)(cnt - 1));
    }
    void find_Badvalue() {
        String badvalue = "坏值有：";
        boolean flag = false;
        double average = average(x, xcount);
        double standard = solve_Bessle(x, xcount) * 3.0D;

        for(int i = 0; i < xcount; ++i) {
            if (abs(x[i] - average) >= standard) {
                flag = true;
                badvalue = badvalue + x[i] + " ";
            }
        }
        if (flag) {
            tf3.setText("3sigma为--->"+standard+"\n平均值为-->"+average+"\n"+badvalue);
        } else {
            tf3.setText("3sigma为--->"+standard+
                    "\n平均值为--->"+average+"\n无坏值");
        }
    }
    double xfangba(double[] data, int cnt) {
        double sum = 0.0D;

        for(int i = 0; i < cnt; ++i) {
            sum += data[i] * data[i];
        }

        return sum / (double)cnt;
    }

    double xyba(double[] x, double[] y, int cnt) {
        double sum = 0.0D;

        for(int i = 0; i < cnt; ++i) {
            sum += x[i] * y[i];
        }

        return sum / (double)cnt;
    }

    double solve_r(double[] x, double[] y, int cnt) {
        double x_avrg = average(x, cnt);
        double y_avrg = average(y, cnt);
        double xfangba = xfangba(x, cnt);
        double yfangba = xfangba(y, cnt);
        double shang = xyba(x, y, cnt) - x_avrg * y_avrg;
        double xia = (xfangba - x_avrg * x_avrg) * (yfangba - y_avrg * y_avrg);
        return shang / sqrt(xia);
    }

    String solve_linefc(double[] x, double[] y, int cnt) {
        double xba = average(x, cnt);
        double yba = average(y, cnt);
        double xyba = xyba(x, y, cnt);
        double xfb = xfangba(x, cnt);
        double b = (xyba - xba * yba) / (xfb - xba * xba);
        double a = yba - b * xba;
        return "y= " + b + "x +" + a;
    }

    public static void main(String[] args) {
        Solve solve = new Solve();
        solve.go();
    }
    public void actionPerformed(ActionEvent e) {
        String method = e.getActionCommand();
        double result;
        switch (method) {
            case "录入数据" -> {
                getXData(tfx.getText());
                getYData(tfy.getText());
                tf3.setText("成功！");
            }
            case "不确定度" -> {
                double uncertainty_A = solve_uncertainty(x, xcount);
                String str_B = JOptionPane.showInputDialog(null,"请输入：\n","请输入B类不确定度",JOptionPane.PLAIN_MESSAGE);
                double uncertainty_B = Double.parseDouble(str_B);
                double uncertainty = sqrt(pow(uncertainty_A,2)+pow(uncertainty_B,2));
                tf3.setText("不确定度A--->"+uncertainty_A+"\n不确定度B为--->"+uncertainty_B
                +"\n不确定度为--->"+uncertainty);
            }
            case "检测1次坏值" -> find_Badvalue();
            case "线性相关系数" -> {
                result = solve_r(x, y, xcount);
                tf3.setText("r = " + result);
            }
            case "线性回归方程" -> {
                String s = solve_linefc(x, y, xcount);
                tf3.setText(s);
            }
            case "清空数据" -> {
                x = new double[20];
                y = new double[20];
                xcount = 0;
                ycount = 0;
                tfx.setText("");
                tfy.setText("");
                tf3.setText("");
            }
        }
    }
}
