package ru.c17.balance.AddReceiptScreen;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SumTextWatcher implements TextWatcher {

    private static final int INTEGER_LENGTH = 10;
    
    
    private String prefix;
    private String postfix;
    private String oldStr;
    private String newStr;

    private EditText et;

    public SumTextWatcher(EditText et)
    {
        this.et = et;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        prefix = s.subSequence(0, start).toString();
        oldStr = s.subSequence(start, start + count).toString();
        postfix = s.subSequence(start + count, s.length()).toString();

    }

    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        newStr = s.subSequence(start, start + count).toString();
    }

    public void afterTextChanged(Editable s)
    {
        et.removeTextChangedListener(this);

        String newTotal = prefix + newStr + postfix;
        String oldTotal = prefix + oldStr + postfix;
        int selPos = et.getSelectionStart();

        if (newStr.equals(".")) {
            if (oldTotal.contains(".")) {
                et.setText(oldTotal);
                et.setSelection(selPos-1);
            } else {
                if (postfix.length() > 2) {
                    float dec = Float.parseFloat("0." + postfix);
                    String decStr = String.valueOf(Math.round(dec * 100));
                    String temp = prefix + newStr + decStr;
                    et.setText(temp);
                    et.setSelection(et.getText().length());
                }
            }
        } else if ("0123456789".contains(newStr) && !newStr.equals("")) {
            if (oldTotal.contains(".")) {
                if (postfix.contains(".")) {
                    if (newTotal.substring(0,newTotal.indexOf(".")).length() > INTEGER_LENGTH) {
                        et.setText(oldTotal);
                        et.setSelection(selPos - 1);
                    }
                } else {
                    String decStr = newTotal.substring(newTotal.indexOf(".") + 1);
                    if (decStr.length() > 2) {
                        et.setText(oldTotal);
                        et.setSelection(selPos - 1);
                    }
                }
            } else {
                if (newTotal.length()> INTEGER_LENGTH) {
                    et.setText(oldTotal);
                    et.setSelection(selPos - 1);
                }
            }
        }
        
        
        et.addTextChangedListener(this);
    }
}
