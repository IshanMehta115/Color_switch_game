package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Arc_serializable extends Arc implements Serializable{}
class Rectangle_serializable extends Rectangle implements Serializable{}
class Label_serializable extends Label implements Serializable{}
class ball_save implements Serializable{
    private int x,y,radius;
    private String color;
    public ball_save(int x,int y,int radius,String color){
        this.x=x;
        this.y=y;
        this.radius =  radius;
        this.color = color;
    }
    public int get_x(){
        return x;
    }
    public int get_y(){
        return y;
    }
    public int get_radius(){
        return radius;
    }
    public String get_color(){
        return color;
    }
}
class star_save implements Serializable{
    private Double[] points;
    private String color;
    private int x,y;
    public star_save(int x,int y,String color,Double[] points){
        this.x = x;
        this.y = y;
        this.color = color;
        this.points = points;
    }
    public int get_x(){
        return x;
    }
    public int get_y(){
        return y;
    }
    public String get_color(){
        return color;
    }
    public Double[] get_points(){
        return points;
    }
}
class color_changer_saver implements Serializable{
    private int x,y,radius,starting_angle;
    private String color;
    public color_changer_saver(int x,int y,int radius,int starting_angle,String color){
        this.x =x;
        this.y= y;
        this.radius = radius;
        this.starting_angle = starting_angle;
        this.color = color;
    }
    public int get_x(){
        return x;
    }
    public int get_y(){
        return y;
    }
    public int get_radius(){
        return radius;
    }
    public int get_angle(){
        return starting_angle;
    }
    public String get_color(){
        return color;
    }
}
class rectangle_saver implements Serializable{
    private int x,y,width,height;
    private String color;
    public rectangle_saver(int x,int y,int width,int height,String color){
        this.x = x;
        this.y =y;
        this.width = width;
        this.height = height;
        this.color = color;
    }
    public int get_x(){
        return x;
    }
    public int get_y(){
        return y;
    }
    public int get_width(){
        return width;
    }
    public int get_height(){
        return height;
    }
    public String get_color(){
        return color;
    }
}
class arc_saver implements Serializable{
    private int x,y,start_angle;
    String color;
    public arc_saver(int x,int y,int start_angle,String color){
        this.x = x;
        this.y =y ;
        this.start_angle = start_angle;
        this.color = color;
    }
    public int get_x(){
        return x;
    }
    public int get_y(){
        return y;
    }
    public int get_angle(){
        return start_angle;
    }
    public String get_color(){
        return color;
    }
}
class player extends Circle implements Serializable{
    private double velocity=0;
    private double acceleration=1;
    private String[] colors_values;
    private ball_save bs;
    public double get_velocity(){
        return this.velocity;
    }
    public void set_velocity(double velocity){
        this.velocity=velocity;
    }
    public double get_acceleration(){
        return this.acceleration;
    }
    private void set_acceleration(double acceleration){
        this.acceleration=acceleration;
    }
    public player(int x,int y,int radius,String[] colors){
        super(x,y,radius,Color.WHITE);
        set_acceleration(0.2);
        set_velocity(0);
        colors_values = new String[4];
        for(int i=0;i<4;i++){
            colors_values[i] = colors[i];
        }
        this.randomize_color();
    }
    public void gravity(){
        set_velocity(get_velocity()+get_acceleration());
    }
    public void move(){
        setCenterY(getCenterY()+get_velocity());
    }
    public void jump(){
        set_velocity(-7);
    }
    public boolean collided(Star star){
        if(Math.abs(star.getLayoutY()-this.getCenterY())<2.5*this.getRadius()){
            star.setLayoutY(star.getLayoutY()-1500);
            return true;
        }
        return false;
    }
    public boolean collided(moving_belt_obstacle mb_obstacle){
        ArrayList<Rectangle_serializable> temp = mb_obstacle.get_obstacles();
        Rectangle obstacle;
        for(int i=0;i<temp.size();i++){
            obstacle = temp.get(i);
            if(this.getFill()==obstacle.getFill())
                continue;
            boolean same_vertically = (Math.abs(this.getCenterY()-obstacle.getY())<this.getRadius()) || (Math.abs(this.getCenterY()-(obstacle.getY()+obstacle.getHeight()))<this.getRadius()) || (obstacle.getY()<=this.getCenterY() && this.getCenterY()<=obstacle.getY()+obstacle.getHeight());
            boolean same_horizontally_right = obstacle.getX()<=this.getCenterX()+this.getRadius() && this.getCenterX()+this.getRadius()<=obstacle.getWidth()+obstacle.getX();
            boolean same_horizontally_left = obstacle.getX()<=this.getCenterX()-this.getRadius() && this.getCenterX()-this.getRadius()<=obstacle.getWidth()+obstacle.getX();
            if(same_vertically){
                if(same_horizontally_right || same_horizontally_left){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean collided(rotating_arc_obstacle ra_obstacle){
        ArrayList<Arc_serializable> temp = ra_obstacle.get_arcs();
        for(int i=0;i<temp.size();i++){
            Arc arc = temp.get(i);
            if(this.getFill()==arc.getStroke())
                continue;
            if((180<=arc.getStartAngle() && arc.getStartAngle()<=270)){ // lower arc
                if((arc.getCenterY()+arc.getRadiusY()-arc.getStrokeWidth()/2.0-this.getRadius() <= this.getCenterY()) && (this.getCenterY()<= arc.getCenterY()+arc.getRadiusY()+arc.getStrokeWidth()/2.0+this.getRadius())){
                    System.out.println("lower");
                    if(arc.getStroke()==Color.RED)
                        System.out.println("RED");
                    if(arc.getStroke()==Color.BLUE)
                        System.out.println("BLUE");
                    if(arc.getStroke()==Color.YELLOW)
                        System.out.println("YELLO");
                    if(arc.getStroke()==Color.GREEN)
                        System.out.println("GREEN");
                    return true;
                }
            }
            else if ((0<=arc.getStartAngle() && arc.getStartAngle()<=90)){ // upper arc
                if((arc.getCenterY()-arc.getRadiusY()-arc.getStrokeWidth()/2.0-this.getRadius()<= this.getCenterY()) && (this.getCenterY() <= arc.getCenterY()-arc.getRadiusY()+arc.getStrokeWidth()/2.0+this.getRadius())){
                    System.out.println("Upper");
                    if(arc.getStroke()==Color.RED)
                        System.out.println("RED");
                    if(arc.getStroke()==Color.BLUE)
                        System.out.println("BLUE");
                    if(arc.getStroke()==Color.YELLOW)
                        System.out.println("YELLO");
                    if(arc.getStroke()==Color.GREEN)
                        System.out.println("GREEN");
                    return true;
                }
            }
        }
        return false;
    }
    public boolean collided(Color_changer color_changer){
        ArrayList<Arc_serializable> temp = color_changer.get_arcs();
        for(int i=0;i<temp.size();i++){
            Arc arc = temp.get(i);
            if(Math.abs(arc.getCenterY()-this.getCenterY())<(this.getRadius()+arc.getRadiusX())){
                color_changer.shift((int)color_changer.get_y()-1500);
                return true;
            }
        }
        return false;
    }
    private Color make_color(int index){
        return Color.web(colors_values[index]);
    }
    public void randomize_color(){
        Paint initial_color = this.getFill();
        Random rand = new Random();
        int index = rand.nextInt(4);
        while(make_color(index)==initial_color){
            index = rand.nextInt(4);
        }
        this.setFill(make_color(index));
    }
    private String get_color_string(){
        if(getFill()==Color.web("red"))
            return "red";
        if(getFill()==Color.web("blue"))
            return "blue";
        if(getFill()==Color.web("yellow"))
            return "yellow";
        if(getFill()==Color.web("green"))
            return "green";

        if(getFill()==Color.web("violet"))
            return "violet";
        if(getFill()==Color.web("yellowgreen"))
            return "yellowgreen";
        if(getFill()==Color.web("TURQUOISE"))
            return "TURQUOISE";

        return "white";
    }
    public void save_ready(){
        bs = new ball_save((int)getCenterX(),(int)getCenterY(),(int)(getRadius()),get_color_string());
    }
    public void reinit(){
        setCenterX(bs.get_x());
        setCenterY(bs.get_y());
        setRadius(bs.get_radius());
        setFill(Color.web(bs.get_color()));

    }
}
class rotating_arc_obstacle implements Serializable{
    private ArrayList<Arc_serializable> arcs;
    private double angular_velocity,arc_length;
    private int number_of_arcs;
    private arc_saver[] as;
    public rotating_arc_obstacle(String[] colors, double screen_width){
        arcs = new ArrayList<>();
        number_of_arcs = 4;
        angular_velocity = 2;
        as = new arc_saver[number_of_arcs];
        arc_length = 360/(double)number_of_arcs;
        for(int i=0;i<number_of_arcs;i++)
            arcs.add(new Arc_serializable());
        for(int i=0;i<number_of_arcs;i++){
            arcs.get(i).setType(ArcType.OPEN);
            arcs.get(i).setCenterX(screen_width/2);
            arcs.get(i).setCenterY(300);
            arcs.get(i).setRadiusY(100.0f);
            arcs.get(i).setRadiusX(100.0f);
            arcs.get(i).setFill(Color.TRANSPARENT);
            arcs.get(i).setStroke(Color.web(colors[i]));
            arcs.get(i).setStrokeWidth(15);
            arcs.get(i).setStartAngle(i*arc_length);
            arcs.get(i).setLength(arc_length);
        }
    }
    private String get_color(Paint color){
        if(color==Color.web("red"))
            return "red";
        if(color==Color.web("blue"))
            return "blue";
        if(color==Color.web("yellow"))
            return "yellow";
        if(color==Color.web("green"))
            return "green";

        if(color==Color.web("violet"))
            return "violet";
        if(color==Color.web("yellowgreen"))
            return "yellowgreen";
        if(color==Color.web("TURQUOISE"))
            return "TURQUOISE";

        return "white";
    }
    public void save_ready(){
        for(int i=0;i<number_of_arcs;i++){
            Arc_serializable temp_arc = arcs.get(i);
            as[i]= new arc_saver((int)temp_arc.getCenterX(),(int)temp_arc.getCenterY(),(int)temp_arc.getStartAngle(),get_color(temp_arc.getStroke()));
        }
    }
    public void reinit(){
        for(int i=0;i<number_of_arcs;i++){
            Arc_serializable temp_arc = new Arc_serializable();
            temp_arc.setCenterX(as[i].get_x());
            temp_arc.setCenterY(as[i].get_y());
            temp_arc.setStartAngle(as[i].get_angle());
            temp_arc.setStroke(Color.web(as[i].get_color()));
            temp_arc.setType(ArcType.OPEN);
            temp_arc.setRadiusY(100.0f);
            temp_arc.setRadiusX(100.0f);
            temp_arc.setFill(Color.TRANSPARENT);
            temp_arc.setStrokeWidth(15);
            temp_arc.setLength(arc_length);
            arcs.set(i,temp_arc);
        }
    }
    public ArrayList<Arc_serializable> get_arcs(){
        return this.arcs;
    }
    public void rotate(){
        for(int i=0;i<number_of_arcs;i++){
            Arc temp = arcs.get(i);
            temp.setStartAngle((temp.getStartAngle()+angular_velocity)%360);
        }
    }
    public void move_down(double velocity){
        for(int i=0;i<number_of_arcs;i++){
            arcs.get(i).setCenterY(arcs.get(i).getCenterY()+velocity);
            if(arcs.get(i).getCenterY()>=900){
                arcs.get(i).setCenterY(-600);
            }
        }
    }
}
class moving_belt_obstacle implements Serializable{
    private ArrayList<Rectangle_serializable> blocks;
    private double velocity_horizontal;
    private int number_of_blocks,block_width,block_height;
    private rectangle_saver[] rs;
    public moving_belt_obstacle(String[] colors){
        blocks = new ArrayList<>();
        number_of_blocks = 8;
        block_width = 150;
        block_height = 25;
        rs = new rectangle_saver[number_of_blocks];
        for(int i=0;i<number_of_blocks;i++){
            blocks.add(new Rectangle_serializable());
        }
        for(int i=0;i<number_of_blocks;i++){
            blocks.get(i).setX(i*block_width);
            blocks.get(i).setY(-300);
            blocks.get(i).setWidth(block_width);
            blocks.get(i).setHeight(block_height);
            blocks.get(i).setFill(Color.web(colors[i%(4)]));
        }
        velocity_horizontal = -3;
    }
    private String get_color(Paint color){
        if(color==Color.web("red"))
            return "red";
        if(color==Color.web("blue"))
            return "blue";
        if(color==Color.web("yellow"))
            return "yellow";
        if(color==Color.web("green"))
            return "green";

        if(color==Color.web("violet"))
            return "violet";
        if(color==Color.web("yellowgreen"))
            return "yellowgreen";
        if(color==Color.web("TURQUOISE"))
            return "TURQUOISE";
        return "white";
    }
    public void save_ready(){
        for(int i=0;i<number_of_blocks;i++){
            Rectangle_serializable temp_rect = blocks.get(i);
            rs[i]= new rectangle_saver((int)temp_rect.getX(),(int)temp_rect.getY(),(int)temp_rect.getWidth(),(int)temp_rect.getHeight(),get_color(temp_rect.getFill()));
        }
    }
    public void reinit(){
        for(int i=0;i<number_of_blocks;i++){
            Rectangle_serializable temp_rect = new Rectangle_serializable();
            temp_rect.setX(rs[i].get_x());
            temp_rect.setY(rs[i].get_y());
            temp_rect.setWidth(rs[i].get_width());
            temp_rect.setHeight(rs[i].get_height());
            temp_rect.setFill(Color.web(rs[i].get_color()));
            blocks.set(i,temp_rect);
        }
    }
    public ArrayList<Rectangle_serializable> get_obstacles(){
        return this.blocks;
    }
    public void move_left(){
        for(int i=0;i<number_of_blocks;i++){
            blocks.get(i).setX(blocks.get(i).getX()+velocity_horizontal);
            if(blocks.get(i).getX()<=-600){
                blocks.get(i).setX(blocks.get(i).getX()+block_width*number_of_blocks);
            }
        }
    }
    public void move_down(double velocity){
        for(int i=0;i<number_of_blocks;i++){
            blocks.get(i).setY(blocks.get(i).getY()-velocity);
            if(blocks.get(i).getY()>=900){
                blocks.get(i).setY(-600);
            }
        }
    }
}
class Star extends Polygon implements Serializable{
    private star_save ss;
    private Double[] temp = new Double[20];
    public Star(double screen_width){
        this.setFill(Color.WHITE);
        temp = new Double[]{
                10.0,  85.0,
                85.0,  75.0,
                110.0, 10.0,
                135.0, 75.0,
                210.0, 85.0,
                160.0, 125.0,
                170.0, 190.0,
                110.0, 150.0,
                50.0 , 190.0,
                60.0 , 125.0,
        };
        for(int i=0;i<temp.length;i++){
            temp[i]*=0.2;
        }
        this.setLayoutX(screen_width/2-100*0.2);
        this.getPoints().addAll(temp);
    }
    public void move_down(double vel){
        this.setLayoutY(getLayoutY()+vel);
        if(this.getLayoutY()>=900){
            this.setLayoutY(-1100);
        }
    }
    public void save_ready(){
        ss = new star_save((int)getLayoutX(),(int)getLayoutY(),"white",temp);
    }
    public void reinit(){
        setLayoutY(ss.get_y());
        setLayoutX(ss.get_x());
        setFill(Color.web(ss.get_color()));
        getPoints().clear();
        getPoints().addAll(ss.get_points());
    }
}
class Color_changer implements Serializable{
    private ArrayList<Arc_serializable> arcs;
    private int number_of_arcs = 4;
    float radius = 20.0f;
    private color_changer_saver[] ccs = new color_changer_saver[4];
    public Color_changer(double screen_width,String[] colors){
        arcs = new ArrayList<>();
        for(int i=0;i<number_of_arcs;i++){
            Arc_serializable temp = new Arc_serializable();
            temp.setCenterX(screen_width/2);
            temp.setRadiusX(radius);
            temp.setRadiusY(radius);
            temp.setStartAngle(i*90);
            temp.setLength(90);
            temp.setType(ArcType.ROUND);
            temp.setFill(Color.web(colors[i]));
            arcs.add(temp);
        }
    }
    public void save_ready(){
        for(int i=0;i<number_of_arcs;i++){
            Arc_serializable temp_arc = arcs.get(i);
            ccs[i] = new color_changer_saver((int)temp_arc.getCenterX(),(int)temp_arc.getCenterY(),(int)temp_arc.getRadiusX(),(int)temp_arc.getStartAngle(),get_color(temp_arc.getFill()));
        }
    }
    public void reinit(){
        for(int i=0;i<number_of_arcs;i++){
            Arc_serializable temp_arc = new Arc_serializable();
            temp_arc.setCenterX(ccs[i].get_x());
            temp_arc.setCenterY(ccs[i].get_y());
            temp_arc.setStartAngle(ccs[i].get_angle());
            temp_arc.setFill(Color.web(ccs[i].get_color()));
            temp_arc.setRadiusX(ccs[i].get_radius());
            temp_arc.setRadiusY(ccs[i].get_radius());
            temp_arc.setLength(90);
            temp_arc.setType(ArcType.ROUND);
            arcs.set(i,temp_arc);
        }
    }
    private String get_color(Paint color){
        if(color==Color.web("red"))
            return "red";
        if(color==Color.web("blue"))
            return "blue";
        if(color==Color.web("yellow"))
            return "yellow";
        if(color==Color.web("green"))
            return "green";

        if(color==Color.web("violet"))
            return "violet";
        if(color==Color.web("yellowgreen"))
            return "yellowgreen";
        if(color==Color.web("TURQUOISE"))
            return "TURQUOISE";
        return "WHITE";
    }
    public ArrayList<Arc_serializable> get_arcs(){
        return  this.arcs;
    }
    public void move_down(double vel){
        for(int i=0;i<number_of_arcs;i++){
            Arc temp = arcs.get(i);
            temp.setCenterY(temp.getCenterY()+vel);
            if(temp.getCenterY()>=900){
                temp.setCenterY(-1100);
            }
        }
    }
    public void shift(int dist){
        for(int i=0;i<number_of_arcs;i++){
            Arc temp = arcs.get(i);
            temp.setCenterY(dist);
        }
    }
    public double get_y(){
        return this.arcs.get(0).getCenterY();
    }

}
class game implements Serializable{
    private String game_name;
    private player ball;
    private Star[] stars;
    private boolean first_time_jump;
    private int score;
    private moving_belt_obstacle  mb_obstacle;
    private boolean started = false;
    private double screen_width,screen_height;
    private boolean game_over = false;
    private Color_changer[] color_changer;
    private rotating_arc_obstacle ra_obstacle;
    public boolean is_game_over(){
        return game_over;
    }
    private static Font text_font = Font.font("ComicSans", FontWeight.BOLD, FontPosture.REGULAR, 25);
    private static Font text_font_bigger = Font.font("ComicSans", FontWeight.BOLD, FontPosture.REGULAR, 30);
    private Label[] texts = new Label[4];
    public game(double screen_width,double screen_height,String[] colors){
        this.screen_width=screen_width;
        this.screen_height=screen_height;
        ball = new player((int)screen_width/2,(int)(screen_height*(0.75)),15,colors);
        first_time_jump=true;
        score = 0;

        mb_obstacle = new moving_belt_obstacle(colors);
        ra_obstacle = new rotating_arc_obstacle(colors,screen_width);
        stars = new Star[2];
        color_changer = new Color_changer[2];
        for(int i=0;i<stars.length;i++){
            stars[i] = new Star(screen_width);
        }
        stars[0].setLayoutY(300-100*0.2);
        stars[1].setLayoutY(-600-100*0.2);

        for(int i=0;i<color_changer.length;i++){
            color_changer[i] = new Color_changer(screen_width,colors);
        }
        color_changer[0].shift(0);
        color_changer[1].shift(-900);


        for(int i=0;i< texts.length;i++){
            texts[i]=new Label_serializable();
        }
        texts[0].setTextAlignment(TextAlignment.CENTER);
        texts[0].setLayoutX(70);
        texts[0].setLayoutY(450);
        texts[0].setFont(text_font_bigger);
        texts[0].setText("Press space to start\nPress p to pause");
        texts[0].setTextFill(Color.LIGHTCORAL);


        texts[1].setTextAlignment(TextAlignment.CENTER);
        texts[1].setLayoutX(160);
        texts[1].setLayoutY(600);
        texts[1].setFont(text_font);
        texts[1].setText("Made by : ");
        texts[1].setTextFill(Color.LIGHTCORAL);

        texts[2].setTextAlignment(TextAlignment.CENTER);
        texts[2].setLayoutX(40);
        texts[2].setLayoutY(650);
        texts[2].setFont(text_font);
        texts[2].setText("Ishan Mehta\n2019309");
        texts[2].setTextFill(Color.LIGHTCORAL);

        texts[3].setTextAlignment(TextAlignment.CENTER);
        texts[3].setLayoutX(screen_width-190);
        texts[3].setLayoutY(650);
        texts[3].setFont(text_font);
        texts[3].setText("Rohit Jangra\n2019444");
        texts[3].setTextFill(Color.LIGHTCORAL);


    }
    public void set_root(Pane root){
        System.out.println("ball center = "+ball.getCenterX());
        root.getChildren().add(ball);
        ArrayList<Rectangle_serializable> temp = mb_obstacle.get_obstacles();
        for(int i=0;i<temp.size();i++){
            root.getChildren().add(temp.get(i));
            System.out.println("adding rects");
            System.out.println("x = " +temp.get(i).getX());
            System.out.println("y = " +temp.get(i).getY());
        }
        for(int i=0;i< stars.length;i++){
            root.getChildren().add(stars[i]);
        }
        for(int i=0;i<color_changer.length;i++){
            ArrayList<Arc_serializable> temp_2 = color_changer[i].get_arcs();
            for(int j=0;j<temp_2.size();j++){
                root.getChildren().add(temp_2.get(j));
            }
        }
        ArrayList<Arc_serializable> temp_3 = ra_obstacle.get_arcs();
        for(int i=0;i<temp_3.size();i++){
            root.getChildren().add(temp_3.get(i));
        }

        for(int i=0;i<texts.length;i++){
            root.getChildren().add(texts[i]);
        }
    }
    public void update(){
        if(started){
            if(ball.collided(stars[0]) || ball.collided((stars[1]))){
                increase_score();
            }
            if(ball.collided(mb_obstacle) || ball.collided(ra_obstacle)){
                game_over=true;
            }
            if(ball.collided(color_changer[0]) || ball.collided((color_changer[1]))){
                ball.randomize_color();
            }
            if(ball.getCenterY()<400 && ball.get_velocity()<0){
                mb_obstacle.move_down(ball.get_velocity());
                for(int i=0;i<stars.length;i++){
                    stars[i].move_down(-ball.get_velocity());
                }
                for(int i=0;i<color_changer.length;i++){
                    color_changer[i].move_down(-ball.get_velocity());
                }
                ra_obstacle.move_down(-ball.get_velocity());
                for(int i=0;i<texts.length;i++){
                    texts[i].setLayoutY(texts[i].getLayoutY()-ball.get_velocity());
                    texts[i].setLayoutY(Math.max(-10,texts[i].getLayoutY()));
                }
            }
            else{
                ball.move();
                if(ball.getCenterY()>=screen_height-ball.getRadius()){
                    game_over=true;
                }
            }
        }
        mb_obstacle.move_left();
        ra_obstacle.rotate();
        ball.gravity();
    }
    public void jump(){
        if(first_time_jump){
            started=true;
            this.ball.jump();
            first_time_jump=false;
        }
    }
    public void released(){
        this.first_time_jump=true;
    }
    public int get_score(){
        return this.score;
    }
    public void increase_score(){
        this.score = this.score+1;
    }
    public void set_name(String name){
        this.game_name=name;
    }
    public String get_name(){
        return this.game_name;
    }
    public void set_start(boolean start){
        this.started = start;
    }
    public player get_ball(){
        return  ball;
    }
    public void save_ready(){
        ball.save_ready();
        for(int i=0;i<stars.length;i++){
            stars[i].save_ready();
        }
        for(int i=0;i<color_changer.length;i++){
            color_changer[i].save_ready();
        }
        mb_obstacle.save_ready();
        ra_obstacle.save_ready();
    }
    public void reinit(){
        ball.reinit();
        for(int i=0;i<stars.length;i++){
            stars[i].reinit();
        }
        for(int i=0;i<color_changer.length;i++){
            color_changer[i].reinit();
        }
        mb_obstacle.reinit();
        ra_obstacle.reinit();
    }
}
public class Main extends Application{
    private static HashMap<String,Scene> scenes = new HashMap<>();
    private static String Color_switch = "Color Switch";
    private static double screen_width=424,screen_height=754;
    private static Font text_font = Font.font("ComicSans", FontWeight.BOLD, FontPosture.REGULAR, 25);
    private static Font heading_font = Font.font("ComicSans", FontWeight.BOLD, FontPosture.REGULAR, 40);
    private static Stage main_stage;
    private static boolean paused=false;
    private static AnimationTimer timer;
    private static game main_game=null;
    private static String[] colors = new String[4];
    private static ListView saved_game_names = new ListView();
    private static ArrayList<game> temp_data;
    private static int theme=0;
    public static Pane get_basic_pane(){
        Pane root = new Pane();
        root.setPrefSize(screen_width,screen_height);
        Image image = new Image("https://i.pinimg.com/originals/2c/58/bd/2c58bdc4e94e027971582711133063be.jpg");

        ImageView imageView = new ImageView(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitHeight(screen_height);
        imageView.setFitWidth(screen_width);

        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);
        return root;
    }
    private static void make_main_menu(Pane root) {
        double width = 400;
        double level = 0;
        double gap = 80;
        double margin = 90;
        Image image = new Image("https://lh3.googleusercontent.com/ROz1vb76-ddQb1XQ0M_YgvSAjR4ItldeMPqso60-NdzL4B47sOYuokiTzVsmrjF5_X3e");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setX(screen_width/2 - width/2);
        imageView.setY(2*20);
        level+=4*gap;

        Label main_menu_text = new Label();
        main_menu_text.setLayoutY(level-50);
        main_menu_text.setPrefWidth(screen_width-2*margin);
        main_menu_text.setTextAlignment(TextAlignment.CENTER);
        main_menu_text.setText("MAIN MENU");
        System.out.println("width = "+main_menu_text.getWidth());
        main_menu_text.setLayoutX(margin);
        main_menu_text.setFont(heading_font);
        main_menu_text.setTextFill(Color.LIGHTCORAL);
        level+=gap/2;

        Button play_button = new Button();
        play_button.setPrefWidth(screen_width-2*margin);
        play_button.setText("PLAY GAME");
        play_button.setFont(text_font);
        play_button.setLayoutX(margin);
        play_button.setLayoutY(level);
        level+=gap;
        play_button.setOnAction(e->{
            start_game();
        });

        Button how_to_play_button = new Button();
        how_to_play_button.setPrefWidth(screen_width-2*margin);
        how_to_play_button.setText("HOW TO PLAY");
        how_to_play_button.setFont(text_font);
        how_to_play_button.setLayoutX(margin);
        how_to_play_button.setLayoutY(level);
        level+=gap;
        how_to_play_button.setOnAction(e->{
            main_stage.setScene(scenes.get("how to play"));
        });


        Button load_game_button = new Button();
        load_game_button.setPrefWidth(screen_width-2*margin);
        load_game_button.setText("LOAD GAME");
        load_game_button.setFont(text_font);
        load_game_button.setLayoutX(margin);
        load_game_button.setLayoutY(level);
        level+=gap;
        load_game_button.setOnAction(e->{
            try {
                set_list_view();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
            main_stage.setScene(scenes.get("load game"));
        });

        Button theme_button = new Button();
        theme_button.setPrefWidth(screen_width-2*margin);
        theme_button.setText("CHANGE THEME");
        theme_button.setFont(text_font);
        theme_button.setLayoutX(margin);
        theme_button.setLayoutY(level);
        level+=gap;
        theme_button.setOnAction(e->{
            main_stage.setScene(scenes.get("themes"));
        });


        Button quit_button = new Button();
        quit_button.setPrefWidth(screen_width-2*margin);
        quit_button.setText("QUIT GAME");
        quit_button.setFont(text_font);
        quit_button.setLayoutX(margin);
        quit_button.setLayoutY(level);

        quit_button.setOnAction(e->{
            System.exit(0);
        });




        root.getChildren().add(imageView);
        root.getChildren().add(main_menu_text);
        root.getChildren().add(play_button);
        root.getChildren().add(how_to_play_button);
        root.getChildren().add(load_game_button);
        root.getChildren().add(theme_button);
        root.getChildren().add(quit_button);
    }
    private static void make_theme(Pane root) {
        double width = 400;
        double level = 0;
        double gap = 80;
        double margin = 90;
        Image image = new Image("https://lh3.googleusercontent.com/ROz1vb76-ddQb1XQ0M_YgvSAjR4ItldeMPqso60-NdzL4B47sOYuokiTzVsmrjF5_X3e");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setX(screen_width/2 - width/2);
        imageView.setY(2*20);
        level+=4*gap;

        Label theme_text = new Label();
        theme_text.setLayoutY(level-50);
        theme_text.setPrefWidth(screen_width-2*margin);
        theme_text.setTextAlignment(TextAlignment.CENTER);
        theme_text.setText("THEMES");
//        System.out.println("width = "+main_menu_text.getWidth());
        theme_text.setLayoutX(margin+50);
        theme_text.setFont(heading_font);
        theme_text.setTextFill(Color.LIGHTCORAL);
        level+=gap/2;

        Button normal_button = new Button();
        normal_button.setPrefWidth(screen_width-2*margin);
        normal_button.setText("NORMAL");
        normal_button.setFont(text_font);
        normal_button.setLayoutX(margin);
        normal_button.setLayoutY(level);
        level+=gap;
        normal_button.setOnAction(e->{
            theme = 0;
            set_color_theme();
            main_stage.setScene(scenes.get("main menu"));
        });

        Rectangle[] normal = new Rectangle[4];
        for(int i=0;i<4;i++){
            normal[i] = new Rectangle();
            normal[i].setX(i*screen_width/4.0);
            normal[i].setY(level);
            normal[i].setWidth(screen_width/4.0);
            normal[i].setHeight(gap);
        }
        normal[0].setFill(Color.web("red"));
        normal[1].setFill(Color.web("blue"));
        normal[2].setFill(Color.web("green"));
        normal[3].setFill(Color.web("yellow"));
        level+=1.5*gap;

        Button flower_button = new Button();
        flower_button.setPrefWidth(screen_width-2*margin);
        flower_button.setText("FLOWER");
        flower_button.setFont(text_font);
        flower_button.setLayoutX(margin);
        flower_button.setLayoutY(level);
        level+=gap;
        flower_button.setOnAction(e->{
            theme = 1;
            set_color_theme();
            main_stage.setScene(scenes.get("main menu"));
        });

        Rectangle[] flower = new Rectangle[4];
        for(int i=0;i<4;i++){
            flower[i] = new Rectangle();
            flower[i].setX(i*screen_width/4.0);
            flower[i].setY(level);
            flower[i].setWidth(screen_width/4.0);
            flower[i].setHeight(gap);
        }
        flower[0].setFill(Color.web("yellow"));
        flower[1].setFill(Color.web("yellowgreen"));
        flower[2].setFill(Color.web("violet"));
        flower[3].setFill(Color.web("TURQUOISE"));






        root.getChildren().add(imageView);
        root.getChildren().add(theme_text);
        root.getChildren().add(normal_button);
        root.getChildren().add(flower_button);
        for(int i=0;i<4;i++){
            root.getChildren().add(flower[i]);
            root.getChildren().add(normal[i]);
        }
    }
    private static void make_how_to_play(Pane root){
        double width = 400;
        double level = 0;
        double gap = 80;
        double margin = 70;
        Image image = new Image("https://lh3.googleusercontent.com/ROz1vb76-ddQb1XQ0M_YgvSAjR4ItldeMPqso60-NdzL4B47sOYuokiTzVsmrjF5_X3e");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setX(screen_width/2 - width/2);
        imageView.setY(2*20);
        level+=4*gap;

        Label how_to_play_heading = new Label();
        how_to_play_heading.setLayoutY(level-50);
        how_to_play_heading.setPrefWidth(screen_width-2*margin);
        how_to_play_heading.setTextAlignment(TextAlignment.CENTER);
        how_to_play_heading.setText("HOW TO PLAY");
        how_to_play_heading.setLayoutX(margin);
        how_to_play_heading.setFont(heading_font);
        how_to_play_heading.setTextFill(Color.LIGHTCORAL);
        level+=gap;

        Label how_to_play_text = new Label();
        how_to_play_text.setWrapText(true);
        how_to_play_text.setLayoutY(level-50);
        how_to_play_text.setPrefWidth(screen_width-2*margin);
        how_to_play_text.setTextAlignment(TextAlignment.CENTER);
        how_to_play_text.setText("Press Space to Jump and get through obstacles of the same color.\nPress p to pause.\nYou can trade 10 stars for an extra life");
        how_to_play_text.setLayoutX(margin);
        how_to_play_text.setFont(text_font);
        how_to_play_text.setTextFill(Color.LIGHTCORAL);
        level+=3.3*gap;

        Button back_button = new Button();
        back_button.setPrefWidth(screen_width-2*margin);
        back_button.setText("BACK");
        back_button.setFont(text_font);
        back_button.setLayoutX(margin);
        back_button.setLayoutY(level);
        back_button.setOnAction(e->{
            main_stage.setScene(scenes.get("main menu"));
        });




        root.getChildren().add(imageView);
        root.getChildren().add(how_to_play_heading);
        root.getChildren().add(how_to_play_text);
        root.getChildren().add(back_button);
    }
    private static void make_load_game(Pane root) throws IOException, ClassNotFoundException {
        double width = 400;
        double level = 0;
        double gap = 80;
        double margin = 90;
        Image image = new Image("https://lh3.googleusercontent.com/ROz1vb76-ddQb1XQ0M_YgvSAjR4ItldeMPqso60-NdzL4B47sOYuokiTzVsmrjF5_X3e");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setX(screen_width/2 - width/2);
        imageView.setY(2*20);
        level+=4*gap;

        Label load_game_text = new Label();
        load_game_text.setLayoutY(level-50);
        load_game_text.setPrefWidth(screen_width-2*margin);
        load_game_text.setTextAlignment(TextAlignment.CENTER);
        load_game_text.setText("LOAD GAME");
        load_game_text.setLayoutX(margin);
        load_game_text.setFont(heading_font);
        load_game_text.setTextFill(Color.LIGHTCORAL);
        level+=gap;

        level+=2*gap;


        Button run_button = new Button();
        run_button.setPrefWidth(screen_width-2*margin);
        run_button.setText("START");
        run_button.setFont(text_font);
        run_button.setLayoutX(margin);
        run_button.setLayoutY(level);
        level+=1*gap;
        run_button.setOnAction(e->{
            try {
                load_all_games();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
            set_game(temp_data,saved_game_names.getSelectionModel().getSelectedItem());
            start_game();
        });

        Button back_button = new Button();
        back_button.setPrefWidth(screen_width-2*margin);
        back_button.setText("BACK");
        back_button.setFont(text_font);
        back_button.setLayoutX(margin);
        back_button.setLayoutY(level);

        back_button.setOnAction(e->{
            main_stage.setScene(scenes.get("main menu"));
        });

        root.getChildren().add(imageView);
        root.getChildren().add(load_game_text);
        root.getChildren().add(saved_game_names);
        root.getChildren().add(run_button);
        root.getChildren().add(back_button);
    }
    private static void make_pause_game(Pane root) {
        double width = 400;
        double level = 0;
        double gap = 80;
        double margin = 70;
        Image image = new Image("https://lh3.googleusercontent.com/ROz1vb76-ddQb1XQ0M_YgvSAjR4ItldeMPqso60-NdzL4B47sOYuokiTzVsmrjF5_X3e");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setX(screen_width/2 - width/2);
        imageView.setY(2*20);
        level+=4*gap;

        Label pause_text = new Label();
        pause_text.setLayoutY(level-50);
        pause_text.setPrefWidth(screen_width-2*margin);
        pause_text.setTextAlignment(TextAlignment.CENTER);
        pause_text.setText("GAME PAUSED");
//        System.out.println("width = "+main_menu_text.getWidth());
        pause_text.setLayoutX(margin);
        pause_text.setFont(heading_font);
        pause_text.setTextFill(Color.LIGHTCORAL);
        level+=gap;

        Button play_button = new Button();
        play_button.setPrefWidth(screen_width-2*margin);
        play_button.setText("RESUME(or press p)");
        play_button.setFont(text_font);
        play_button.setLayoutX(margin);
        play_button.setLayoutY(level);
        level+=gap;
        play_button.setOnAction(e->{
            paused = false;
            main_stage.setScene(scenes.get("game scene"));
        });

        Button save_button = new Button();
        save_button.setPrefWidth(screen_width-2*margin);
        save_button.setText("SAVE GAME");
        save_button.setFont(text_font);
        save_button.setLayoutX(margin);
        save_button.setLayoutY(level);
        level+=gap;
        save_button.setOnAction(e->{
            main_stage.setScene(scenes.get("save game"));
        });


        Button restart_button = new Button();
        restart_button.setPrefWidth(screen_width-2*margin);
        restart_button.setText("RESTART");
        restart_button.setFont(text_font);
        restart_button.setLayoutX(margin);
        restart_button.setLayoutY(level);
        level+=gap;
        restart_button.setOnAction(e->{
            timer.stop();
            start_game();
        });

        Button quit_button = new Button();
        quit_button.setPrefWidth(screen_width-2*margin);
        quit_button.setText("BACK TO MENU");
        quit_button.setFont(text_font);
        quit_button.setLayoutX(margin);
        quit_button.setLayoutY(level);
        quit_button.setOnAction(e->{
            timer.stop();
            main_stage.setScene(scenes.get("main menu"));
        });




        root.getChildren().add(imageView);
        root.getChildren().add(pause_text);
        root.getChildren().add(play_button);
        root.getChildren().add(save_button);
        root.getChildren().add(restart_button);
        root.getChildren().add(quit_button);
    }
    private static void make_save_game(Pane root) {
        double width = 400;
        double level = 0;
        double gap = 80;
        double margin = 90;
        Image image = new Image("https://lh3.googleusercontent.com/ROz1vb76-ddQb1XQ0M_YgvSAjR4ItldeMPqso60-NdzL4B47sOYuokiTzVsmrjF5_X3e");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setX(screen_width/2 - width/2);
        imageView.setY(2*20);
        level+=4*gap;

        Label save_game_text = new Label();
        save_game_text.setLayoutY(level-50);
        save_game_text.setPrefWidth(screen_width-2*margin);
        save_game_text.setTextAlignment(TextAlignment.CENTER);
        save_game_text.setText("SAVE GAME");
        save_game_text.setLayoutX(margin);
        save_game_text.setFont(heading_font);
        save_game_text.setTextFill(Color.LIGHTCORAL);
        level+=gap;


        TextField game_name = new TextField();
        game_name.setPrefWidth(screen_width-2*margin);
        game_name.setLayoutX(margin);
        game_name.setLayoutY(level);
        game_name.setFont(text_font);
        game_name.setPromptText("Enter game name");
        game_name.setFocusTraversable(false);
        game_name.setStyle("-fx-prompt-text-fill: lightcoral");
        level+=gap;



        Button save_button = new Button();
        save_button.setPrefWidth(screen_width-2*margin);
        save_button.setText("SAVE");
        save_button.setFont(text_font);
        save_button.setLayoutX(margin);
        save_button.setLayoutY(level);
        level+=gap;
        save_button.setOnAction(e->{
            try {
                if(game_name.getText()==null){
                    game_name.setPromptText("Enter game name");
                }
                else{
                    save_game(game_name.getText());
                    main_stage.setScene(scenes.get("game saved"));
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });


        Button back_button = new Button();
        back_button.setPrefWidth(screen_width-2*margin);
        back_button.setText("BACK");
        back_button.setFont(text_font);
        back_button.setLayoutX(margin);
        back_button.setLayoutY(level);
        back_button.setOnAction(e->{
            main_stage.setScene(scenes.get("main menu"));
        });

        root.getChildren().add(imageView);
        root.getChildren().add(save_game_text);
        root.getChildren().add(game_name);
        root.getChildren().add(save_button);
        root.getChildren().add(back_button);
    }
    private static void make_game_saved(Pane root){
        double width = 400;
        double level = 0;
        double gap = 80;
        double margin = 70;
        Image image = new Image("https://lh3.googleusercontent.com/ROz1vb76-ddQb1XQ0M_YgvSAjR4ItldeMPqso60-NdzL4B47sOYuokiTzVsmrjF5_X3e");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setX(screen_width/2 - width/2);
        imageView.setY(2*20);
        level+=4*gap;

        Label game_text = new Label();
        game_text.setLayoutY(level-50);
        game_text.setPrefWidth(screen_width-2*margin);
        game_text.setTextAlignment(TextAlignment.CENTER);
        game_text.setText("GAME SAVED");
        game_text.setLayoutX(margin);
        game_text.setFont(heading_font);
        game_text.setTextFill(Color.LIGHTCORAL);
        level+=gap;


        Button back_button = new Button();
        back_button.setPrefWidth(screen_width-2*margin);
        back_button.setText("BACK");
        back_button.setFont(text_font);
        back_button.setLayoutX(margin);
        back_button.setLayoutY(level);
        back_button.setOnAction(e->{
            timer.stop();
            main_game=null;
            main_stage.setScene(scenes.get("main menu"));
        });

        root.getChildren().add(imageView);
        root.getChildren().add(game_text);
        root.getChildren().add(back_button);
    }
    private static void set_color_theme(){
        if(theme==0){
            colors[0]="RED";
            colors[1]="GREEN";
            colors[2]="YELLOW";
            colors[3]="BLUE";
        }
        else if(theme==1){
            colors[0]="yellow";
            colors[2]="yellowgreen";
            colors[1]="violet";
            colors[3]="TURQUOISE";
        }
    }
    private static void initialize() throws IOException, ClassNotFoundException {



        Pane temp;
        set_color_theme();
        temp = get_basic_pane();
        make_main_menu(temp);
        Scene main_menu_scene = new Scene(temp);
        scenes.put("main menu",main_menu_scene);

        temp = get_basic_pane();
        make_how_to_play(temp);
        Scene how_to_play_scene = new Scene(temp);
        scenes.put("how to play",how_to_play_scene);

        temp = get_basic_pane();
        make_load_game(temp);
        Scene load_game_scene = new Scene(temp);
        scenes.put("load game",load_game_scene);

        temp = get_basic_pane();
        Scene play_game_scene = new Scene(temp);
        scenes.put("play game",play_game_scene);

        temp = get_basic_pane();
        make_pause_game(temp);
        Scene pause_scene = new Scene(temp);
        pause_scene.setOnKeyPressed(e->{
            switch (e.getCode()){
                case P:
                    main_stage.setScene(scenes.get("game scene"));
                    paused = false;
                    break;
            }
        });
        scenes.put("pause game",pause_scene);

        temp = get_basic_pane();
        make_save_game(temp);
        Scene save_game = new Scene(temp);
        scenes.put("save game",save_game);

        temp = get_basic_pane();
        make_game_saved(temp);
        Scene game_saved = new Scene(temp);
        scenes.put("game saved",game_saved);

        temp = get_basic_pane();
        make_theme(temp);
        Scene themes = new Scene(temp);
        scenes.put("themes",themes);

    }
    public static void start_game(){
        System.out.println("start_game");
        paused = false;
        Pane root = get_basic_pane();
        if(main_game==null){
            System.out.println("game was null");
            main_game = new game(screen_width,screen_height,colors);
        }
        main_game.set_root(root);
        Label score_label = new Label();
        score_label.setLayoutY(20);
        score_label.setLayoutX(20);
        score_label.setTextAlignment(TextAlignment.CENTER);
        final int[] prev_score = {0};
        score_label.setText("Score: "+main_game.get_score());
        score_label.setFont(heading_font);
        score_label.setTextFill(Color.LIGHTCORAL);
        root.getChildren().add(score_label);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(!paused){
                    main_game.update();
                    if(prev_score[0] !=main_game.get_score()){
                        score_label.setText("Score: "+main_game.get_score());
                        prev_score[0] = main_game.get_score();
                    }
                    if(main_game.is_game_over()){
                        System.out.println("game over guys");
                        game_over(main_game.get_score());
                        main_game=null;
                        stop();
                    }
                }
            }
        };
        timer.start();
        Scene game_scene = new Scene(root);
        scenes.put("game scene",game_scene);
        game_scene.setOnKeyPressed(e->{
            switch(e.getCode()){
                case SPACE:
                    main_game.jump();
                    break;
                case P:
                    paused = true;
                    main_stage.setScene(scenes.get("pause game"));
                    break;
            }
        });
        game_scene.setOnKeyReleased(e->{
            switch (e.getCode()){
                case SPACE:
                    main_game.released();
            }
        });
        main_stage.setScene(game_scene);
    }
    private static void make_game_over(Pane root,int score){
        double width = 400;
        double level = 0;
        double gap = 80;
        double margin = 90;
        Image image = new Image("https://lh3.googleusercontent.com/ROz1vb76-ddQb1XQ0M_YgvSAjR4ItldeMPqso60-NdzL4B47sOYuokiTzVsmrjF5_X3e");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setX(screen_width/2 - width/2);
        imageView.setY(2*20);
        level+=4*gap;

        Label game_over_text = new Label();
        game_over_text.setLayoutY(level-50);
        game_over_text.setPrefWidth(screen_width-2*margin);
        game_over_text.setTextAlignment(TextAlignment.CENTER);
        game_over_text.setText("GAME OVER");
        game_over_text.setLayoutX(margin);
        game_over_text.setFont(heading_font);
        game_over_text.setTextFill(Color.LIGHTCORAL);
        level+=gap;

        Label score_text = new Label();
        score_text.setWrapText(true);
        score_text.setLayoutY(level-50);
        score_text.setPrefWidth(screen_width-2*margin);
        score_text.setTextAlignment(TextAlignment.CENTER);
        score_text.setText("Your Score was : "+score);
        score_text.setLayoutX(margin);
        score_text.setFont(text_font);
        score_text.setTextFill(Color.LIGHTCORAL);
        level+=gap;



        Button restart_button = new Button();
        restart_button.setPrefWidth(screen_width-2*margin);
        restart_button.setText("RESTART");
        restart_button.setFont(text_font);
        restart_button.setLayoutX(margin);
        restart_button.setLayoutY(level);
        restart_button.setOnAction(e->{
            System.out.println("here");
            start_game();
        });
        level+=gap;

        Button back_button = new Button();
        back_button.setPrefWidth(screen_width-2*margin);
        back_button.setText("BACK");
        back_button.setFont(text_font);
        back_button.setLayoutX(margin);
        back_button.setLayoutY(level);
        back_button.setOnAction(e->{
            main_stage.setScene(scenes.get("main menu"));
        });

        root.getChildren().add(imageView);
        root.getChildren().add(game_over_text);
        root.getChildren().add(score_text);
        root.getChildren().add(restart_button);
        root.getChildren().add(back_button);
    }
    private static void game_over(int score){
        Pane temp = get_basic_pane();
        make_game_over(temp,score);
        Scene game_over_scene = new Scene(temp);
        main_stage.setScene(game_over_scene);
    }
    public static void save_game(String game_name) throws IOException, ClassNotFoundException {
        main_game.set_name(game_name);
        main_game.save_ready();
        File f = new File("saved_games.txt");
        ArrayList<game> data = new ArrayList<>();
        if(f.exists()){
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (ArrayList<game>) ois.readObject();
            ois.close();
            System.out.println("appending");
        }
        data.add(main_game);
        FileOutputStream fos =  new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(data);
        oos.close();
    }
    public static void load_all_games() throws IOException, ClassNotFoundException {
        File f = new File("saved_games.txt");
        ArrayList<game> data = null;
        if(f.exists()){
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (ArrayList<game>) ois.readObject();
            ois.close();
        }
        temp_data = data;
    }
    private static void set_list_view() throws IOException, ClassNotFoundException {
        saved_game_names.getItems().clear();
        load_all_games();
        if(temp_data!=null){
            for(int i=0;i<temp_data.size();i++){
                saved_game_names.getItems().add(temp_data.get(i).get_name());
                System.out.println("name = "+temp_data.get(i).get_name());
            }
        }
        saved_game_names.setPrefWidth(screen_width-2*90);
        saved_game_names.setPrefHeight(2*60);
        saved_game_names.setLayoutY(400);
        saved_game_names.setLayoutX(90);
    }
    public static void set_game(ArrayList<game> temp , Object name){
        String game_name = (String)(name);
        System.out.println("load game name = "+game_name);
        System.out.println("temp size = "+temp.size());
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).get_name().equals(game_name)){
                System.out.println("found game");
                main_game = temp.get(i);
                main_game.set_start(false);
                main_game.reinit();
                System.out.println("ball center = "+main_game.get_ball().getCenterX());
            }
        }
    }
    @Override
    public void start(Stage primaryStage){
        main_stage = primaryStage;
        primaryStage.setTitle(Color_switch);
        primaryStage.setScene(scenes.get("main menu"));
        primaryStage.show();
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        initialize();
        launch(args);
    }
}