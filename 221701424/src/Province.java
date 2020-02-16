import java.text.Collator;
import java.util.Locale;

/**
 * province
 */
public class Province {

    private String name;
    private int num_infect;
    private int num_cure;
    private int num_dead;
    private int num_suspect;

    public Province(String name) {
        this.name = name;
        this.num_cure = 0;
        this.num_dead = 0;
        this.num_infect = 0;
        this.num_suspect = 0;
    }
    public Province(String name, int num_cure, int num_dead, int num_infect, int num_suspect) {
        this.name = name;
        this.num_cure = num_cure;
        this.num_dead = num_dead;
        this.num_infect = num_infect;
        this.num_suspect = num_suspect;
    }

    public String toString(Commands cmds) {
        String message = "";
        if (cmds.province.isEmpty()) {
            message = this.name;
            if (cmds.type.isEmpty()) {
                message += " 感染患者 " + this.num_infect + "人 疑似患者 " + this.num_suspect
                + "人 治愈 " + this.num_cure + "人 死亡 " + this.num_dead + "人";
            }
            else {
                for (String s : cmds.type) {
                    if (s.equals("ip")) {
                        message += " 感染患者 " + this.num_infect + "人 ";
                    }
                    else if (s.equals("sp")) {
                        message += " 疑似患者 " + this.num_suspect + "人 ";
                    }
                    else if (s.equals("cure")) {
                        message += " 治愈 " + this.num_cure + "人 ";
                    }
                    else {
                        message += " 死亡 " + this.num_dead + "人 ";
                    }
                }
                String a = FileUtil.saveDigit(message);
                int b = Integer.valueOf(a);
                if (b == 0) {
                    message = "";
                }
            }            
        }
        else {
            if (cmds.type.isEmpty()) {
                for (String p : cmds.province) {
                    if (this.name.equals(p)) {
                        //message = this.name;
                        message = this.name + " 感染患者 " + this.num_infect + "人 疑似患者 " + this.num_suspect
                            + "人 治愈 " + this.num_cure + "人 死亡 " + this.num_dead + "人"; 
                        break;
                    }
                }
            }
            else {
                for (String p : cmds.province) {
                    if (this.name.equals(p)) {
                        message = this.name;
                        for (String s : cmds.type) {
                            if (s.equals("ip")) {
                                message += " 感染患者 " + this.num_infect + "人 ";
                            }
                            else if (s.equals("sp")) {
                                message += " 疑似患者 " + this.num_suspect + "人 ";
                            }
                            else if (s.equals("cure")) {
                                message += " 治愈 " + this.num_cure + "人 ";
                            }
                            else {
                                message += " 死亡 " + this.num_dead + "人 ";
                            }
                        }
                        break;
                    }  
                }           
            }
        }       
        return message;
    }

    //按拼音排序
    public int compareByName(Province p) {
        Collator c = Collator.getInstance(Locale.CHINA);
        int numForName = c.compare(this.getName(), p.getName());
        return numForName;
    }

    public void setName (String name) {
        this.name = name;
    }
    public void addNumInfect (int num) {
        this.num_infect += num;
    }
    public void addNumSuspect (int num) {
        this.num_suspect += num;
    }
    public void addNumCure (int num) {
        this.num_cure += num;
    }
    public void addNumDead (int num) {
        this.num_dead += num;
    }
    public void subNumInfect (int num) {
        this.num_infect -= num;
    }
    public void subNumSuspect (int num) {
        this.num_suspect -= num;
    }
    public void subNumCure (int num) {
        this.num_cure -= num;
    }
    public void subNumDead (int num) {
        this.num_dead -= num;
    }
    public void setNumInfect (int num) {
        this.num_infect = num;
    }
    public void setNumSuspect (int num) {
        this.num_suspect = num;
    }
    public void setNumCure (int num) {
        this.num_cure = num;
    }
    public void setNumDead (int num) {
        this.num_dead = num;
    }

    public String getName() {
        return this.name;
    }
    public int getNumInfect() {
        return this.num_infect;
    }
    public int getNumSuspect() {
        return this.num_suspect;
    }
    public int getNumCure() {
        return this.num_cure;
    }
    public int getNumDead() {
        return this.num_dead;
    }

    
    
    
}