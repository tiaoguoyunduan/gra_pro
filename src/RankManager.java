import java.io.*;
import java.util.*;

public class RankManager{
    private String playerName=null;              //当前玩家姓名
    private int playerNum=0,player=0,matchNum=0; //玩家数量、当前玩家编号、比赛场次
    private String[] namel;                      //玩家列表
    private List<String> name=new ArrayList<>();
    private int[] matchScore,score;              //比赛分数，队伍积分
    public void readRank(String playerName){
        this.playerName=playerName;
        try {
            Scanner input = new Scanner(new File("F:\\gra_pro\\project\\test\\rank.dat"));
            playerNum = Integer.parseInt(input.next());
            matchNum=playerNum*(playerNum-1)/2;
            if (playerNum != 0) {
                for (int i = 0; i < playerNum; i++) {
                    name.add(input.next());
                }
                this.player=name.indexOf(playerName);
                if(player==-1){
                    name.add(playerName);
                    namel=name.toArray(new String[0]);
                    score=new int[playerNum+1];
                    matchScore=new int[matchNum+playerNum];
                    name.remove(playerNum);
                }else{
                    namel=name.toArray(new String[0]);
                    score=new int[playerNum];
                    matchScore=new int[matchNum];
                    name.remove(player);
                }
                for(int i=0;i<playerNum;i++) score[i]=Integer.parseInt(input.next());
                for (int i = 0; i < matchNum; i++){
                    matchScore[i]=Integer.parseInt(input.next());
                }
            }else {

            }
            input.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String[] getList(){
        return name.toArray(new String[0]);
    }
    public void match(int result[]){
        int myScore=0;
        if(player==-1){
            for(int i=0;i<playerNum;i++){
                matchScore[matchNum+i]=result[i];
                if(result[i]==0){
                    score[i]++;
                }else {}
                myScore=myScore+result[i];
            }
            score[playerNum]=myScore;
            playerNum++;
        }else {
            int i=0,p=player*(player-1)/2,s=0;
            for(;i<player;i++) {
                score[i]=score[i]+matchScore[p]-result[i];
                matchScore[p]=result[i];
                myScore=myScore+result[i];
                p++;
            }
            i++;
            p=i*(i+1)/2-1;
            for(;i<playerNum;i++) {
                s=1-result[i-1];
                score[i]=score[i]+s-matchScore[p];
                matchScore[p]=s;
                myScore=myScore+result[i-1];
                p=p+i;
            }
            score[player]=myScore;
        }

    }
    public void writeRank(){
        try {
            String nameS="",scoreS="",matchScoreS="";
            int p=0;
            for(int i=0;i<playerNum;i++) {
                nameS = nameS + namel[i] + " ";
                scoreS = scoreS + score[i] + " ";
                for (int t = 0; t < i; t++) {
                    matchScoreS = matchScoreS + matchScore[p] + " ";
                    p++;
                }
                matchScoreS = matchScoreS + "\r\n";
            }
            PrintWriter out = new PrintWriter(new FileWriter("F:\\gra_pro\\project\\test\\rank.dat"));
            out.print(playerNum+"\r\n");
            out.println(nameS);
            out.println(scoreS);
            out.print(matchScoreS);
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static String getRank(){
        try{
            Scanner input = new Scanner(new File("F:\\gra_pro\\project\\test\\rank.dat"));
            int playerNum=Integer.parseInt(input.next());
            //out.print(playerNum);
            String[] name=new String[playerNum];
            int[] score=new int[playerNum];
            String n,message="";
            int s;
            for(int i=0;i<playerNum;i++){
                name[i]=input.next();
            }
            for(int i=0;i<playerNum;i++){
                score[i]=Integer.parseInt(input.next());
            }
            input.close();
            for(int i=0;i<playerNum-1;i++){
                for(int j=0;j<playerNum-1-i;j++){
                    if(score[j]<score[j+1]){
                        s=score[j];
                        n=name[j];
                        score[j]=score[j+1];
                        name[j]=name[j+1];
                        score[j+1]=s;
                        name[j+1]=n;
                    }
                }
            }
            for(int i=0;i<playerNum;i++){
                message=message+"<tr><th>"+name[i]+"</th><th>"+score[i]+"</th></tr>";
            }
            return message;
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }
    /*
    public static void main(String args[]){
        Scanner in =new Scanner(System.in);
        while(true){
            RankManager rankManager=new RankManager();
            rankManager.readRank(in.next());
            int[] sc;
            int a=rankManager.playerNum;
            if(rankManager.player==-1){
                sc=new int[a];
            }else {
                a--;
                sc=new int[a];
            }
            Random ra=new Random();
            for(int i=0;i<a;i++) {
                sc[i]=ra.nextInt(2);
                System.out.println(sc[i]);
            }
            System.out.println(rankManager.name.toString());
            rankManager.match(sc);
            rankManager.writeRank();
        }
    }
    */
}