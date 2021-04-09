package practice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.*;

public class Solve extends JFrame implements ActionListener {
    double[] t = new double[]{0.0, 0.0, 1.84, 1.32, 1.2, 1.14, 1.11, 1.09, 1.08, 1.07, 1.06, 1.05, 1.03};
    double[] x = new double[20];
    double[] y = new double[20];
    int xcount;
    int ycount;
    JTextField tfx = new JTextField();
    JTextField tfy = new JTextField();
    JTextField tf3 = new JTextField();
    JLabel label3 = new JLabel("cj.Wang");
    JLabel label1 = new JLabel("主要输入框(x)");
    JLabel label2 = new JLabel("可选输入框(y)");
    JButton button0 = new JButton("录入数据");
    JButton button1 = new JButton("不确定度A");
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
        this.label1.setBounds(30, 0, 300, 30);
        this.label2.setBounds(30, 90, 300, 30);
        this.label3.setBounds(430, 420, 120, 60);
        this.button0.setBounds(30, 410, 100, 50);
        this.button1.setBounds(350, 30, 120, 30);
        this.button2.setBounds(350, 80, 120, 30);
        this.button3.setBounds(350, 130, 120, 30);
        this.button4.setBounds(350, 180, 120, 30);
        this.button5.setBounds(200, 410, 100, 50);
        this.button0.addActionListener(this);
        this.button1.addActionListener(this);
        this.button2.addActionListener(this);
        this.button3.addActionListener(this);
        this.button4.addActionListener(this);
        this.button5.addActionListener(this);
        this.tfx.setBounds(30, 30, 300, 50);
        this.tfy.setBounds(30, 120, 300, 50);
        this.tf3.setBounds(30, 200, 300, 200);
        this.tf3.setEditable(false);
        frame.getContentPane().add(this.button0);
        frame.getContentPane().add(this.button1);
        frame.getContentPane().add(this.button2);
        frame.getContentPane().add(this.button3);
        frame.getContentPane().add(this.button4);
        frame.getContentPane().add(this.button5);
        frame.getContentPane().add(this.label1);
        frame.getContentPane().add(this.label2);
        frame.getContentPane().add(this.label3);
        frame.getContentPane().add(this.tf3);
        frame.getContentPane().add(this.tfx);
        frame.getContentPane().add(this.tfy);
        frame.setVisible(true);
    }

    double solve_uncertainty(double[] data, int cnt) {
        double s = this.solve_Bessle(data, cnt);
        return this.t[cnt] * s / Math.sqrt(cnt);
    }

    void getXData(String s) {
        StringBuilder temp = new StringBuilder();
        int len = s.length();

        for(int i = 0; i < len; ++i) {
            if (s.charAt(i) == ' ') {
                this.x[this.xcount++] = Double.parseDouble(temp.toString());
                temp = new StringBuilder();
            } else if (i == len - 1) {
                temp.append(s.charAt(i));
                this.x[this.xcount++] = Double.parseDouble(temp.toString());
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
                this.y[this.ycount++] = Double.parseDouble(temp.toString());
                temp = new StringBuilder();
            } else if (i == len - 1) {
                temp.append(s.charAt(i));
                this.y[this.ycount++] = Double.parseDouble(temp.toString());
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
        double avrg = this.average(data, cnt);
        double sum = 0.0D;

        for(int i = 0; i < cnt; ++i) {
            sum += Math.pow(data[i] - avrg, 2.0D);
        }

        return Math.sqrt(sum / (double)(cnt - 1));
    }

    void find_Badvalue() {
        String badvalue = "坏值有：";
        boolean flag = false;
        double average = this.average(this.x, this.xcount);
        double standard = this.solve_Bessle(this.x, this.xcount) * 3.0D;

        for(int i = 0; i < this.xcount; ++i) {
            if (Math.abs(this.x[i] - average) >= standard) {
                flag = true;
                badvalue = badvalue + this.x[i] + " ";
            }
        }
        if (flag) {
            this.tf3.setText("2sigma为--->"+standard+"\n"+badvalue);
        } else {
            this.tf3.setText("2sigma为--->"+standard+"\n无坏值");
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
        double x_avrg = this.average(x, cnt);
        double y_avrg = this.average(y, cnt);
        double xfangba = this.xfangba(x, cnt);
        double yfangba = this.xfangba(y, cnt);
        double shang = this.xyba(x, y, cnt) - x_avrg * y_avrg;
        double xia = (xfangba - x_avrg * x_avrg) * (yfangba - y_avrg * y_avrg);
        return shang / Math.sqrt(xia);
    }

    String solve_linefc(double[] x, double[] y, int cnt) {
        double xba = this.average(x, cnt);
        double yba = this.average(y, cnt);
        double xyba = this.xyba(x, y, cnt);
        double xfb = this.xfangba(x, cnt);
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
                this.getXData(this.tfx.getText());
                this.getYData(this.tfy.getText());
                this.tf3.setText("成功！");
            }
            case "不确定度A" -> {
                result = this.solve_uncertainty(this.x, this.xcount);
                DecimalFormat myformat = new DecimalFormat("0.0000000");
                String str = myformat.format(result);
                this.tf3.setText(str);
            }
            case "检测1次坏值" -> this.find_Badvalue();
            case "线性相关系数" -> {
                result = this.solve_r(this.x, this.y, this.xcount);
                this.tf3.setText("r = " + result);
            }
            case "线性回归方程" -> {
                String s = this.solve_linefc(this.x, this.y, this.xcount);
                this.tf3.setText(s);
            }
            case "清空数据" -> {
                this.x = new double[20];
                this.y = new double[20];
                this.xcount = 0;
                this.ycount = 0;
                this.tfx.setText("");
                this.tfy.setText("");
                this.tf3.setText("");
            }
        }
    }
}
