package com.mzeat.util;

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

//import com.android.mms.R;


import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mzeat.R;

/**
 * A class for annotating a CharSequence with spans to convert textual emoticons
 * to graphical ones.
 */
public class SmileyParser {
    // Singleton stuff

    private static SmileyParser sInstance;
    public static SmileyParser getInstance() { return sInstance; }
    public static void init(Context context) {
        sInstance = new SmileyParser(context);
    }

    private final Context mContext;
    private final String[] mSmileyTexts;
    private final Pattern mPattern;
    private final HashMap<String, Integer> mSmileyToRes;

    private SmileyParser(Context context) {
        mContext = context;
        mSmileyTexts = mContext.getResources().getStringArray(DEFAULT_SMILEY_TEXTS);
        mSmileyToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }
    
    public static class Smileys {
    	//表情图片集合
        public static final int[] sIconIds = {
            R.drawable.aoman,
            R.drawable.baiyan,
            R.drawable.bishi,
            R.drawable.bizui,
            R.drawable.cahan,
            R.drawable.caidao,
            R.drawable.chajin,
            R.drawable.cheer,
            R.drawable.chong,
            R.drawable.ciya,
            
            R.drawable.da,
            R.drawable.dabian,
            R.drawable.dabing,
            R.drawable.dajiao,
            R.drawable.daku,
            R.drawable.dangao,
            R.drawable.fanu,
            R.drawable.dao,
            R.drawable.deyi,
            R.drawable.diaoxie,
            
            R.drawable.er,
            R.drawable.fadai,     
            R.drawable.fadou,
            R.drawable.fan,
            R.drawable.feiwen,
            R.drawable.fendou,
            R.drawable.gangga,
            R.drawable.geili,
            R.drawable.gouyin,
            R.drawable.guzhang,
            
            R.drawable.haha,
            R.drawable.haixiu,
            R.drawable.haqian,     
            R.drawable.hua,
            R.drawable.huaixiao,
            R.drawable.huishou,
            R.drawable.huitou,
            R.drawable.jidong,
            R.drawable.jingkong,
            R.drawable.jingya,
            
            R.drawable.kafei,
            R.drawable.keai,
            R.drawable.kelian,
            R.drawable.ketou,     
            R.drawable.kiss,
            R.drawable.ku,
            R.drawable.kuaikule,
            R.drawable.kulou,
            R.drawable.kun,
            R.drawable.lanqiu,
            
            R.drawable.lenghan,
            R.drawable.liuhan,
            R.drawable.liulei,
            R.drawable.liwu,
            R.drawable.love,     
            R.drawable.ma,
            R.drawable.nanguo,
            R.drawable.no,
            R.drawable.ok,
            R.drawable.peifu,
            
            R.drawable.pijiu,
            R.drawable.pingpang,
            R.drawable.pizui,
            R.drawable.qiang,
            R.drawable.qinqin,
            R.drawable.qioudale,  
            R.drawable.qiu,
            R.drawable.quantou,
            R.drawable.ruo,
            R.drawable.se,
            
            R.drawable.shandian,
            R.drawable.shengli,
            R.drawable.shuai,
            R.drawable.shuijiao,  
            R.drawable.taiyang,  
        };
        //将图片映射为 文字
        public static int aoman = 0;
        public static int baiyan = 1;
        public static int bishi = 2;
        public static int bizui = 3;
        public static int cahan = 4;
        public static int caidao = 5;
        public static int chajin = 6;
        public static int cheer = 7;
        public static int chong = 8;
        public static int ciya = 9;
 
        public static int da = 10;
        public static int dabian = 11;
        public static int dabing = 12;
        public static int dajiao = 13;
        public static int daku = 14;
        public static int dangao = 15;
        public static int fanu = 16;
        public static int dao = 17;
        public static int deyi = 18;
        public static int diaoxie = 19;
        

        public static int er = 20;
        public static int fadai = 21;
        public static int fadou = 22;
        public static int fan = 23;
        public static int feiwen = 24;
        public static int fendou = 25;
        public static int gangga = 26;
        public static int geili = 27;
        public static int gouyin = 28;
        public static int guzhang = 29;
        

        public static int haha = 30;
        public static int haixiu = 31;
        public static int haqian = 32;
        public static int hua = 33;
        public static int huaixiao = 34;
        public static int huishou = 35;
        public static int huitou = 36;
        public static int jidong = 37;
        public static int jingkong = 38;
        public static int jingya = 39;
        
        public static int kafei = 40;
        public static int keai = 41;
        public static int kelian = 42;
        public static int ketou = 43;
        public static int kiss = 44;
        public static int ku = 45;
        public static int kuaikule = 46;
        public static int kulou = 47;
        public static int kun = 48;
        public static int lanqiu = 49;
  
        public static int lenghan = 50;
        public static int liuhan = 51;
        public static int liulei = 52;
        public static int liwu = 53;
        public static int love = 54;
        public static int ma = 55;
        public static int nanguo = 56;
        public static int no = 57;
        public static int ok = 58;
        public static int peifu = 59;
        
 
        public static int pijiu = 60;
        public static int pingpang = 61;
        public static int pizui = 62;
        public static int qiang = 63;
        public static int qinqin = 64;
        public static int qioudale = 65; 
        public static int qiu = 66;
        public static int quantou = 67;
        public static int ruo = 68;
        public static int se = 69;

        public static int shandian = 70;
        public static int shengli = 71;
        public static int shuai = 72;
        public static int shuijiao = 73;
        public static int taiyang = 74;
        
        
        
        
 
 
        //得到图片表情 根据id
        public static int getSmileyResource(int which) {
            return sIconIds[which];
        }
    }

    // NOTE: if you change anything about this array, you must make the corresponding change

    // to the string arrays: default_smiley_texts and default_smiley_names in res/values/arrays.xml

    public static final int[] DEFAULT_SMILEY_RES_IDS = {
        Smileys.getSmileyResource(Smileys.aoman), // 0
        Smileys.getSmileyResource(Smileys.baiyan), // 1
        Smileys.getSmileyResource(Smileys.bishi), // 2
        Smileys.getSmileyResource(Smileys.bizui), // 3
        Smileys.getSmileyResource(Smileys.cahan), // 4
        Smileys.getSmileyResource(Smileys.caidao), // 5
        Smileys.getSmileyResource(Smileys.chajin), // 6
        Smileys.getSmileyResource(Smileys.cheer), // 7
        Smileys.getSmileyResource(Smileys.chong), // 8
        Smileys.getSmileyResource(Smileys.ciya), // 9
        
        Smileys.getSmileyResource(Smileys.da), // 1
        Smileys.getSmileyResource(Smileys.dabian), // 1
        Smileys.getSmileyResource(Smileys.dabing), // 2
        Smileys.getSmileyResource(Smileys.dajiao), // 3
        Smileys.getSmileyResource(Smileys.daku), // 4
        Smileys.getSmileyResource(Smileys.dangao), // 5
        Smileys.getSmileyResource(Smileys.fanu), // 6
        Smileys.getSmileyResource(Smileys.dao), // 7
        Smileys.getSmileyResource(Smileys.deyi), // 8
        Smileys.getSmileyResource(Smileys.diaoxie), // 9
  
        Smileys.getSmileyResource(Smileys.er), // 2
        Smileys.getSmileyResource(Smileys.fadai), // 1
        Smileys.getSmileyResource(Smileys.fadou), // 2
        Smileys.getSmileyResource(Smileys.fan), // 3
        Smileys.getSmileyResource(Smileys.feiwen), // 4
        Smileys.getSmileyResource(Smileys.fendou), // 5
        Smileys.getSmileyResource(Smileys.gangga), // 6
        Smileys.getSmileyResource(Smileys.geili), // 7
        Smileys.getSmileyResource(Smileys.gouyin), // 8
        Smileys.getSmileyResource(Smileys.guzhang), // 9

        Smileys.getSmileyResource(Smileys.haha), // 3
        Smileys.getSmileyResource(Smileys.haixiu), // 1
        Smileys.getSmileyResource(Smileys.haqian), // 2
        Smileys.getSmileyResource(Smileys.hua), // 3
        Smileys.getSmileyResource(Smileys.huaixiao), // 4
        Smileys.getSmileyResource(Smileys.huishou), // 5
        Smileys.getSmileyResource(Smileys.huitou), // 6
        Smileys.getSmileyResource(Smileys.jidong), // 7
        Smileys.getSmileyResource(Smileys.jingkong), // 8
        Smileys.getSmileyResource(Smileys.jingya), // 9
        
  
        Smileys.getSmileyResource(Smileys.kafei), // 4
        Smileys.getSmileyResource(Smileys.keai), // 1
        Smileys.getSmileyResource(Smileys.kelian), // 2
        Smileys.getSmileyResource(Smileys.ketou), // 3
        Smileys.getSmileyResource(Smileys.kiss), // 4
        Smileys.getSmileyResource(Smileys.ku), // 5
        Smileys.getSmileyResource(Smileys.kuaikule), // 6
        Smileys.getSmileyResource(Smileys.kulou), // 7
        Smileys.getSmileyResource(Smileys.kun), // 8
        Smileys.getSmileyResource(Smileys.lanqiu), // 9
  
        Smileys.getSmileyResource(Smileys.lenghan), // 5
        Smileys.getSmileyResource(Smileys.liuhan), // 1
        Smileys.getSmileyResource(Smileys.liulei), // 2
        Smileys.getSmileyResource(Smileys.liwu), // 3
        Smileys.getSmileyResource(Smileys.love), // 4
        Smileys.getSmileyResource(Smileys.ma), // 5
        Smileys.getSmileyResource(Smileys.nanguo), // 6
        Smileys.getSmileyResource(Smileys.no), // 7
        Smileys.getSmileyResource(Smileys.ok), // 8
        Smileys.getSmileyResource(Smileys.peifu), // 9

        Smileys.getSmileyResource(Smileys.pijiu), // 6
        Smileys.getSmileyResource(Smileys.pingpang), // 1
        Smileys.getSmileyResource(Smileys.pizui), // 2
        Smileys.getSmileyResource(Smileys.qiang), // 3
        Smileys.getSmileyResource(Smileys.qinqin), // 4
        Smileys.getSmileyResource(Smileys.qioudale), // 5
        Smileys.getSmileyResource(Smileys.qiu), // 6
        Smileys.getSmileyResource(Smileys.quantou), // 7
        Smileys.getSmileyResource(Smileys.ruo), // 8
        Smileys.getSmileyResource(Smileys.se), // 9
      
        Smileys.getSmileyResource(Smileys.shandian), // 0
        Smileys.getSmileyResource(Smileys.shengli), // 1
        Smileys.getSmileyResource(Smileys.shuai), // 2
        Smileys.getSmileyResource(Smileys.shuijiao), // 3
        Smileys.getSmileyResource(Smileys.taiyang), // 4

        

    };

    public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;
    //public static final int DEFAULT_SMILEY_NAMES = R.array.default_smiley_names;

    /**
     * Builds the hashtable we use for mapping the string version
     * of a smiley (e.g. ":-)") to a resource ID for the icon version.
     */
    public HashMap<String, Integer> buildSmileyToRes() {
        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
            // Throw an exception if someone updated DEFAULT_SMILEY_RES_IDS

            // and failed to update arrays.xml

            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }

        HashMap<String, Integer> smileyToRes =
                            new HashMap<String, Integer>(mSmileyTexts.length);
        for (int i = 0; i < mSmileyTexts.length; i++) {
            smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);
        }

        return smileyToRes;
    }

    
    /**
     * Builds the regular expression we use to find smileys in {@link #addSmileySpans}.
     */
    //构建正则表达式
    private Pattern buildPattern() {
        // Set the StringBuilder capacity with the assumption that the average

        // smiley is 3 characters long.

        StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);

        // Build a regex that looks like (:-)|:-(|...), but escaping the smilies

        // properly so they will be interpreted literally by the regex matcher.

        patternString.append('(');
        for (String s : mSmileyTexts) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        // Replace the extra '|' with a ')'

        patternString.replace(patternString.length() - 1, patternString.length(), ")");

        return Pattern.compile(patternString.toString());
    }


    /**
     * Adds ImageSpans to a CharSequence that replace textual emoticons such
     * as :-) with a graphical version.
     *
     * @param text A CharSequence possibly containing emoticons
     * @return A CharSequence annotated with ImageSpans covering any
     * recognized emoticons.
     */
    //根据文本替换成图片
    public CharSequence addSmileySpans(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            builder.setSpan(new ImageSpan(mContext, resId),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
}



