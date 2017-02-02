/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jl.atys.chat.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hrb.jl.pinai.R;

public class SmileUtils {
    public static final String emoji_1 = "[:1]";
    public static final String emoji_2 = "[:2]";
    public static final String emoji_3 = "[:3]";
    public static final String emoji_4 = "[:4]";
    public static final String emoji_5 = "[:5]";
    public static final String emoji_6 = "[:6]";
    public static final String emoji_7 = "[:7]";
    public static final String emoji_8 = "[:8]";
    public static final String emoji_9 = "[:9]";
    public static final String emoji_10 = "[:10]";
    public static final String emoji_11 = "[:11]";
    public static final String emoji_12 = "[:12]";
    public static final String emoji_13 = "[:13]";
    public static final String emoji_14 = "[:14]";
    public static final String emoji_15 = "[:15]";
    public static final String emoji_16 = "[:16]";
    public static final String emoji_17 = "[:17]";
    public static final String emoji_18 = "[:18]";
    public static final String emoji_19 = "[:19]";
    public static final String emoji_20 = "[:20]";
    public static final String emoji_21 = "[:21]";
    public static final String emoji_22 = "[:22]";
    public static final String emoji_23 = "[:23]";
    public static final String emoji_24 = "[:24]";
    public static final String emoji_25 = "[:25]";
    public static final String emoji_26 = "[:26]";
    public static final String emoji_27 = "[:27]";
    public static final String emoji_28 = "[:28]";
    public static final String emoji_29 = "[:29]";
    public static final String emoji_30 = "[:30]";
    public static final String emoji_31 = "[:31]";
    public static final String emoji_32 = "[:32]";
    public static final String emoji_33 = "[:33]";
    public static final String emoji_34 = "[:34]";
    public static final String emoji_35 = "[:35]";
    public static final String emoji_36 = "[:36]";
    public static final String emoji_37 = "[:37]";
    public static final String emoji_38 = "[:38]";
    public static final String emoji_39 = "[:39]";
    public static final String emoji_40 = "[:40]";
    public static final String emoji_41 = "[:41]";
    public static final String emoji_42 = "[:42]";
    public static final String emoji_43 = "[:43]";
    public static final String emoji_44 = "[:44]";
    public static final String emoji_45 = "[:45]";
    public static final String emoji_46 = "[:46]";
    public static final String emoji_47 = "[:47]";
    public static final String emoji_48 = "[:48]";
    public static final String emoji_49 = "[:49]";
    public static final String emoji_50 = "[:50]";
    public static final String emoji_51 = "[:51]";
    public static final String emoji_52 = "[:52]";
    public static final String emoji_53 = "[:53]";
    public static final String emoji_54 = "[:54]";
    public static final String emoji_55 = "[:55]";
    public static final String emoji_56 = "[:56]";
    public static final String emoji_57 = "[:57]";
    public static final String emoji_58 = "[:58]";
    public static final String emoji_59 = "[:59]";
    public static final String emoji_60 = "[:60]";
    public static final String emoji_61 = "[:61]";
    public static final String emoji_62 = "[:62]";
    public static final String emoji_63 = "[:63]";
    public static final String emoji_64 = "[:64]";
    public static final String emoji_65 = "[:65]";
    public static final String emoji_66 = "[:66]";
    public static final String emoji_67 = "[:67]";
    public static final String emoji_68 = "[:68]";
    public static final String emoji_69 = "[:69]";
    public static final String emoji_70 = "[:70]";
    public static final String emoji_71 = "[:71]";
    public static final String emoji_72 = "[:72]";
    public static final String emoji_73 = "[:73]";
    public static final String emoji_74 = "[:74]";
    public static final String emoji_75 = "[:75]";
    public static final String emoji_76 = "[:76]";
    public static final String emoji_77 = "[:77]";
    public static final String emoji_78 = "[:78]";
    public static final String emoji_79 = "[:79]";
    public static final String emoji_80 = "[:80]";
    public static final String emoji_81 = "[:81]";
    public static final String emoji_82 = "[:82]";
    public static final String emoji_83 = "[:83]";
    public static final String emoji_84 = "[:84]";
    public static final String emoji_85 = "[:85]";
    public static final String emoji_86 = "[:86]";
    public static final String emoji_87 = "[:87]";
    public static final String emoji_88 = "[:88]";
    public static final String emoji_89 = "[:89]";
    public static final String emoji_90 = "[:90]";
    public static final String emoji_91 = "[:91]";
    public static final String emoji_92 = "[:92]";
    public static final String emoji_93 = "[:93]";
    public static final String emoji_94 = "[:94]";
    public static final String emoji_95 = "[:95]";
    public static final String emoji_96 = "[:96]";
    public static final String emoji_97 = "[:97]";
    public static final String emoji_98 = "[:98]";
    public static final String emoji_99 = "[:99]";
    public static final String emoji_100 = "[:100]";
    public static final String emoji_101 = "[:101]";
    public static final String emoji_102 = "[:102]";
    public static final String emoji_103 = "[:103]";
    public static final String emoji_104 = "[:104]";
    public static final String emoji_105 = "[:105]";
    public static final String emoji_106 = "[:106]";
    public static final String emoji_107 = "[:107]";
    public static final String emoji_108 = "[:108]";
    public static final String emoji_109 = "[:109]";
    public static final String emoji_110 = "[:110]";
    public static final String emoji_111 = "[:111]";
    public static final String emoji_112 = "[:112]";
    public static final String emoji_113 = "[:113]";
    public static final String emoji_114 = "[:114]";
    public static final String emoji_115 = "[:115]";
    public static final String emoji_116 = "[:116]";
    public static final String emoji_117 = "[:117]";
    public static final String emoji_118 = "[:118]";
    public static final String emoji_119 = "[:119]";
    public static final String emoji_120 = "[:120]";
    public static final String emoji_121 = "[:121]";
    public static final String emoji_122 = "[:122]";
    public static final String emoji_123 = "[:123]";
    public static final String emoji_124 = "[:124]";
    public static final String emoji_125 = "[:125]";
    public static final String emoji_126 = "[:126]";
    public static final String emoji_127 = "[:127]";
    public static final String emoji_128 = "[:128]";
    public static final String emoji_129 = "[:129]";
    public static final String emoji_130 = "[:130]";
    public static final String emoji_131 = "[:131]";
    public static final String emoji_132 = "[:132]";
    public static final String emoji_133 = "[:133]";
    public static final String emoji_134 = "[:134]";
    public static final String emoji_135 = "[:135]";
    public static final String emoji_136 = "[:136]";
    public static final String emoji_137 = "[:137]";
    public static final String emoji_138 = "[:138]";
    public static final String emoji_139 = "[:139]";
    public static final String emoji_140 = "[:140]";
    public static final String emoji_141 = "[:141]";
    public static final String emoji_142 = "[:142]";
    public static final String emoji_143 = "[:143]";
    public static final String emoji_144 = "[:144]";
    public static final String emoji_145 = "[:145]";
    public static final String emoji_146 = "[:146]";
    public static final String emoji_147 = "[:147]";
    public static final String emoji_148 = "[:148]";
    public static final String emoji_149 = "[:149]";
    public static final String emoji_150 = "[:150]";
    public static final String emoji_151 = "[:151]";
    public static final String emoji_152 = "[:152]";
    public static final String emoji_153 = "[:153]";
    public static final String emoji_154 = "[:154]";
    public static final String emoji_155 = "[:155]";
    public static final String emoji_156 = "[:156]";
    public static final String emoji_157 = "[:157]";
    public static final String emoji_158 = "[:158]";
    public static final String emoji_159 = "[:159]";
    public static final String emoji_160 = "[:160]";
    public static final String emoji_161 = "[:161]";
    public static final String emoji_162 = "[:162]";
    public static final String emoji_163 = "[:163]";
    public static final String emoji_164 = "[:164]";
    public static final String emoji_165 = "[:165]";
    public static final String emoji_166 = "[:166]";
    public static final String emoji_167 = "[:167]";
    public static final String emoji_168 = "[:168]";
    public static final String emoji_169 = "[:169]";
    public static final String emoji_170 = "[:170]";
    public static final String emoji_171 = "[:171]";
    public static final String emoji_172 = "[:172]";
    public static final String emoji_173 = "[:173]";
    public static final String emoji_174 = "[:174]";
    public static final String emoji_175 = "[:175]";
    public static final String emoji_176 = "[:176]";
    public static final String emoji_177 = "[:177]";
    public static final String emoji_178 = "[:178]";
    public static final String emoji_179 = "[:179]";
    public static final String emoji_180 = "[:180]";
    public static final String emoji_181 = "[:181]";
    public static final String emoji_182 = "[:182]";
    public static final String emoji_183 = "[:183]";
    public static final String emoji_184 = "[:184]";
    public static final String emoji_185 = "[:185]";
    public static final String emoji_186 = "[:186]";
    public static final String emoji_187 = "[:187]";
    public static final String emoji_188 = "[:188]";
    public static final String emoji_189 = "[:189]";
    public static final String emoji_190 = "[:190]";
    public static final String emoji_191 = "[:191]";
    public static final String emoji_192 = "[:192]";
    public static final String emoji_193 = "[:193]";
    public static final String emoji_194 = "[:194]";
    public static final String emoji_195 = "[:195]";
    public static final String emoji_196 = "[:196]";

    private static final Factory spannableFactory = Factory
            .getInstance();
    private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

    static {
        addPattern(emoticons, emoji_1, R.drawable.emoji_1);
        addPattern(emoticons, emoji_2, R.drawable.emoji_2);
        addPattern(emoticons, emoji_3, R.drawable.emoji_3);
        addPattern(emoticons, emoji_4, R.drawable.emoji_4);
        addPattern(emoticons, emoji_5, R.drawable.emoji_5);
        addPattern(emoticons, emoji_6, R.drawable.emoji_6);
        addPattern(emoticons, emoji_7, R.drawable.emoji_7);
        addPattern(emoticons, emoji_8, R.drawable.emoji_8);
        addPattern(emoticons, emoji_9, R.drawable.emoji_9);
        addPattern(emoticons, emoji_10, R.drawable.emoji_10);
        addPattern(emoticons, emoji_11, R.drawable.emoji_11);
        addPattern(emoticons, emoji_12, R.drawable.emoji_12);
        addPattern(emoticons, emoji_13, R.drawable.emoji_13);
        addPattern(emoticons, emoji_14, R.drawable.emoji_14);
        addPattern(emoticons, emoji_15, R.drawable.emoji_15);
        addPattern(emoticons, emoji_16, R.drawable.emoji_16);
        addPattern(emoticons, emoji_17, R.drawable.emoji_17);
        addPattern(emoticons, emoji_18, R.drawable.emoji_18);
        addPattern(emoticons, emoji_19, R.drawable.emoji_19);
        addPattern(emoticons, emoji_20, R.drawable.emoji_20);
        addPattern(emoticons, emoji_21, R.drawable.emoji_21);
        addPattern(emoticons, emoji_22, R.drawable.emoji_22);
        addPattern(emoticons, emoji_23, R.drawable.emoji_23);
        addPattern(emoticons, emoji_24, R.drawable.emoji_24);
        addPattern(emoticons, emoji_25, R.drawable.emoji_25);
        addPattern(emoticons, emoji_26, R.drawable.emoji_26);
        addPattern(emoticons, emoji_27, R.drawable.emoji_27);
        addPattern(emoticons, emoji_28, R.drawable.emoji_28);
        addPattern(emoticons, emoji_29, R.drawable.emoji_29);
        addPattern(emoticons, emoji_30, R.drawable.emoji_30);
        addPattern(emoticons, emoji_31, R.drawable.emoji_31);
        addPattern(emoticons, emoji_32, R.drawable.emoji_32);
        addPattern(emoticons, emoji_33, R.drawable.emoji_33);
        addPattern(emoticons, emoji_34, R.drawable.emoji_34);
        addPattern(emoticons, emoji_35, R.drawable.emoji_35);
        addPattern(emoticons, emoji_36, R.drawable.emoji_36);
        addPattern(emoticons, emoji_37, R.drawable.emoji_37);
        addPattern(emoticons, emoji_38, R.drawable.emoji_38);
        addPattern(emoticons, emoji_39, R.drawable.emoji_39);
        addPattern(emoticons, emoji_40, R.drawable.emoji_40);
        addPattern(emoticons, emoji_41, R.drawable.emoji_41);
        addPattern(emoticons, emoji_42, R.drawable.emoji_42);
        addPattern(emoticons, emoji_43, R.drawable.emoji_43);
        addPattern(emoticons, emoji_44, R.drawable.emoji_44);
        addPattern(emoticons, emoji_45, R.drawable.emoji_45);
        addPattern(emoticons, emoji_46, R.drawable.emoji_46);
        addPattern(emoticons, emoji_47, R.drawable.emoji_47);
        addPattern(emoticons, emoji_48, R.drawable.emoji_48);
        addPattern(emoticons, emoji_49, R.drawable.emoji_49);
        addPattern(emoticons, emoji_50, R.drawable.emoji_50);
        addPattern(emoticons, emoji_51, R.drawable.emoji_51);
        addPattern(emoticons, emoji_52, R.drawable.emoji_52);
        addPattern(emoticons, emoji_53, R.drawable.emoji_53);
        addPattern(emoticons, emoji_54, R.drawable.emoji_54);
        addPattern(emoticons, emoji_55, R.drawable.emoji_55);
        addPattern(emoticons, emoji_56, R.drawable.emoji_56);
        addPattern(emoticons, emoji_57, R.drawable.emoji_57);
        addPattern(emoticons, emoji_58, R.drawable.emoji_58);
        addPattern(emoticons, emoji_59, R.drawable.emoji_59);
        addPattern(emoticons, emoji_60, R.drawable.emoji_60);
        addPattern(emoticons, emoji_61, R.drawable.emoji_61);
        addPattern(emoticons, emoji_62, R.drawable.emoji_62);
        addPattern(emoticons, emoji_63, R.drawable.emoji_63);
        addPattern(emoticons, emoji_64, R.drawable.emoji_64);
        addPattern(emoticons, emoji_65, R.drawable.emoji_65);
        addPattern(emoticons, emoji_66, R.drawable.emoji_66);
        addPattern(emoticons, emoji_67, R.drawable.emoji_67);
        addPattern(emoticons, emoji_68, R.drawable.emoji_68);
        addPattern(emoticons, emoji_69, R.drawable.emoji_69);
        addPattern(emoticons, emoji_70, R.drawable.emoji_70);
        addPattern(emoticons, emoji_71, R.drawable.emoji_71);
        addPattern(emoticons, emoji_72, R.drawable.emoji_72);
        addPattern(emoticons, emoji_73, R.drawable.emoji_73);
        addPattern(emoticons, emoji_74, R.drawable.emoji_74);
        addPattern(emoticons, emoji_75, R.drawable.emoji_75);
        addPattern(emoticons, emoji_76, R.drawable.emoji_76);
        addPattern(emoticons, emoji_77, R.drawable.emoji_77);
        addPattern(emoticons, emoji_78, R.drawable.emoji_78);
        addPattern(emoticons, emoji_79, R.drawable.emoji_79);
        addPattern(emoticons, emoji_80, R.drawable.emoji_80);
        addPattern(emoticons, emoji_81, R.drawable.emoji_81);
        addPattern(emoticons, emoji_82, R.drawable.emoji_82);
        addPattern(emoticons, emoji_83, R.drawable.emoji_83);
        addPattern(emoticons, emoji_84, R.drawable.emoji_84);
        addPattern(emoticons, emoji_85, R.drawable.emoji_85);
        addPattern(emoticons, emoji_86, R.drawable.emoji_86);
        addPattern(emoticons, emoji_87, R.drawable.emoji_87);
        addPattern(emoticons, emoji_88, R.drawable.emoji_88);
        addPattern(emoticons, emoji_89, R.drawable.emoji_89);
        addPattern(emoticons, emoji_90, R.drawable.emoji_90);
        addPattern(emoticons, emoji_91, R.drawable.emoji_91);
        addPattern(emoticons, emoji_92, R.drawable.emoji_92);
        addPattern(emoticons, emoji_93, R.drawable.emoji_93);
        addPattern(emoticons, emoji_94, R.drawable.emoji_94);
        addPattern(emoticons, emoji_95, R.drawable.emoji_95);
        addPattern(emoticons, emoji_96, R.drawable.emoji_96);
        addPattern(emoticons, emoji_97, R.drawable.emoji_97);
        addPattern(emoticons, emoji_98, R.drawable.emoji_98);
        addPattern(emoticons, emoji_99, R.drawable.emoji_99);
        addPattern(emoticons, emoji_100, R.drawable.emoji_100);
        addPattern(emoticons, emoji_101, R.drawable.emoji_101);
        addPattern(emoticons, emoji_102, R.drawable.emoji_102);
        addPattern(emoticons, emoji_103, R.drawable.emoji_103);
        addPattern(emoticons, emoji_104, R.drawable.emoji_104);
        addPattern(emoticons, emoji_105, R.drawable.emoji_105);
        addPattern(emoticons, emoji_106, R.drawable.emoji_106);
        addPattern(emoticons, emoji_107, R.drawable.emoji_107);
        addPattern(emoticons, emoji_108, R.drawable.emoji_108);
        addPattern(emoticons, emoji_109, R.drawable.emoji_109);
        addPattern(emoticons, emoji_110, R.drawable.emoji_110);
        addPattern(emoticons, emoji_111, R.drawable.emoji_111);
        addPattern(emoticons, emoji_112, R.drawable.emoji_112);
        addPattern(emoticons, emoji_113, R.drawable.emoji_113);
        addPattern(emoticons, emoji_114, R.drawable.emoji_114);
        addPattern(emoticons, emoji_115, R.drawable.emoji_115);
        addPattern(emoticons, emoji_116, R.drawable.emoji_116);
        addPattern(emoticons, emoji_117, R.drawable.emoji_117);
        addPattern(emoticons, emoji_118, R.drawable.emoji_118);
        addPattern(emoticons, emoji_119, R.drawable.emoji_119);
        addPattern(emoticons, emoji_120, R.drawable.emoji_120);
        addPattern(emoticons, emoji_121, R.drawable.emoji_121);
        addPattern(emoticons, emoji_122, R.drawable.emoji_122);
        addPattern(emoticons, emoji_123, R.drawable.emoji_123);
        addPattern(emoticons, emoji_124, R.drawable.emoji_124);
        addPattern(emoticons, emoji_125, R.drawable.emoji_125);
        addPattern(emoticons, emoji_126, R.drawable.emoji_126);
        addPattern(emoticons, emoji_127, R.drawable.emoji_127);
        addPattern(emoticons, emoji_128, R.drawable.emoji_128);
        addPattern(emoticons, emoji_129, R.drawable.emoji_129);
        addPattern(emoticons, emoji_130, R.drawable.emoji_130);
        addPattern(emoticons, emoji_131, R.drawable.emoji_131);
        addPattern(emoticons, emoji_132, R.drawable.emoji_132);
        addPattern(emoticons, emoji_133, R.drawable.emoji_133);
        addPattern(emoticons, emoji_134, R.drawable.emoji_134);
        addPattern(emoticons, emoji_135, R.drawable.emoji_135);
        addPattern(emoticons, emoji_136, R.drawable.emoji_136);
        addPattern(emoticons, emoji_137, R.drawable.emoji_137);
        addPattern(emoticons, emoji_138, R.drawable.emoji_138);
        addPattern(emoticons, emoji_139, R.drawable.emoji_139);
        addPattern(emoticons, emoji_140, R.drawable.emoji_140);
        addPattern(emoticons, emoji_141, R.drawable.emoji_141);
        addPattern(emoticons, emoji_142, R.drawable.emoji_142);
        addPattern(emoticons, emoji_143, R.drawable.emoji_143);
        addPattern(emoticons, emoji_144, R.drawable.emoji_144);
        addPattern(emoticons, emoji_145, R.drawable.emoji_145);
        addPattern(emoticons, emoji_146, R.drawable.emoji_146);
        addPattern(emoticons, emoji_147, R.drawable.emoji_147);
        addPattern(emoticons, emoji_148, R.drawable.emoji_148);
        addPattern(emoticons, emoji_149, R.drawable.emoji_149);
        addPattern(emoticons, emoji_150, R.drawable.emoji_150);
        addPattern(emoticons, emoji_151, R.drawable.emoji_151);
        addPattern(emoticons, emoji_152, R.drawable.emoji_152);
        addPattern(emoticons, emoji_153, R.drawable.emoji_153);
        addPattern(emoticons, emoji_154, R.drawable.emoji_154);
        addPattern(emoticons, emoji_155, R.drawable.emoji_155);
        addPattern(emoticons, emoji_156, R.drawable.emoji_156);
        addPattern(emoticons, emoji_157, R.drawable.emoji_157);
        addPattern(emoticons, emoji_158, R.drawable.emoji_158);
        addPattern(emoticons, emoji_159, R.drawable.emoji_159);
        addPattern(emoticons, emoji_160, R.drawable.emoji_160);
        addPattern(emoticons, emoji_161, R.drawable.emoji_161);
        addPattern(emoticons, emoji_162, R.drawable.emoji_162);
        addPattern(emoticons, emoji_163, R.drawable.emoji_163);
        addPattern(emoticons, emoji_164, R.drawable.emoji_164);
        addPattern(emoticons, emoji_165, R.drawable.emoji_165);
        addPattern(emoticons, emoji_166, R.drawable.emoji_166);
        addPattern(emoticons, emoji_167, R.drawable.emoji_167);
        addPattern(emoticons, emoji_168, R.drawable.emoji_168);
        addPattern(emoticons, emoji_169, R.drawable.emoji_169);
        addPattern(emoticons, emoji_170, R.drawable.emoji_170);
        addPattern(emoticons, emoji_171, R.drawable.emoji_171);
        addPattern(emoticons, emoji_172, R.drawable.emoji_172);
        addPattern(emoticons, emoji_173, R.drawable.emoji_173);
        addPattern(emoticons, emoji_174, R.drawable.emoji_174);
        addPattern(emoticons, emoji_175, R.drawable.emoji_175);
        addPattern(emoticons, emoji_176, R.drawable.emoji_176);
        addPattern(emoticons, emoji_177, R.drawable.emoji_177);
        addPattern(emoticons, emoji_178, R.drawable.emoji_178);
        addPattern(emoticons, emoji_179, R.drawable.emoji_179);
        addPattern(emoticons, emoji_180, R.drawable.emoji_180);
        addPattern(emoticons, emoji_181, R.drawable.emoji_181);
        addPattern(emoticons, emoji_182, R.drawable.emoji_182);
        addPattern(emoticons, emoji_183, R.drawable.emoji_183);
        addPattern(emoticons, emoji_184, R.drawable.emoji_184);
        addPattern(emoticons, emoji_185, R.drawable.emoji_185);
        addPattern(emoticons, emoji_186, R.drawable.emoji_186);
        addPattern(emoticons, emoji_187, R.drawable.emoji_187);
        addPattern(emoticons, emoji_188, R.drawable.emoji_188);
        addPattern(emoticons, emoji_189, R.drawable.emoji_189);
        addPattern(emoticons, emoji_190, R.drawable.emoji_190);
        addPattern(emoticons, emoji_191, R.drawable.emoji_191);
        addPattern(emoticons, emoji_192, R.drawable.emoji_192);
        addPattern(emoticons, emoji_193, R.drawable.emoji_193);
        addPattern(emoticons, emoji_194, R.drawable.emoji_194);
        addPattern(emoticons, emoji_195, R.drawable.emoji_195);
        addPattern(emoticons, emoji_196, R.drawable.emoji_196);

    }

    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable, int size) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Drawable drawable = context.getResources().getDrawable(entry.getValue());
                    drawable.setBounds(0, 0, size, size);//这里设置图片的大小
                    spannable.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable, 60);
        return spannable;
    }

    /**
     * 文字转换成表情
     *
     * @param context
     * @param text    文字
     * @param size    表情大小
     * @return
     */
    public static Spannable getSmiledText(Context context, CharSequence text, int size) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable, size);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }
}
