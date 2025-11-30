package simpledb.file;
import java.util.*;
import java.io.*;

public class Stats {
    private int nReads;
    private int nWrites;


    public int getReads() {
        return this.nReads;
    }

    public int getWrites() {
        return this.nWrites;
    }

    public void updateReads(){
        this.nReads = this.nReads + 1;
    }

    public void updateWrites(){
        this.nWrites = this.nWrites + 1;
    }

    public void reset(){
        this.nReads = 0;
        this.nWrites = 0;
    }
}
