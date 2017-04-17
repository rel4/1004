package com.hongbaogou.dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hongbaogou.R;
import com.hongbaogou.utils.ToastUtil;

/**
 * Created by Administrator on 2015/12/25.
 * <p/>
 * 修改数量的dialog
 */
public class UpdateBuyCountDiolag extends DialogFragment {

    private Button sure;
    private Button cancel;
    private ImageView minus;
    private ImageView add;
    private EditText buy;

    private int buyCount;
    private int defaultCount = 10;
    private int stepSize = 1;
    private int maxSize;
    private boolean isTen = false;
    private OnSureListener onSureListener;

    public void setOnSureListener(OnSureListener onSureListener) {
        this.onSureListener = onSureListener;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setDefaultCount(int defaultCount) {
        this.defaultCount = defaultCount;
    }

    public void setIsTen(boolean isTen) {
        this.isTen = isTen;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.updatebuycountdiolag, container);

        buy = (EditText) view.findViewById(R.id.buy);
        buy.setText(String.valueOf(defaultCount));
        buy.selectAll();
        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        sure = (Button) view.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (onSureListener != null) {
                    String str = buy.getText().toString();
                    if (str.length() == 0) {
                        buyCount = defaultCount;
                    } else {
                        int inputCount = Integer.parseInt(str);
                        if (inputCount > maxSize) {
                            buyCount = maxSize;
                            ToastUtil.showToast(getContext(), R.string.buyhint);
                        } else {
                            if (isTen) {
                                if (!str.endsWith("0")) {
                                    str = str.substring(0, str.length() - 1) + "0";
                                    buyCount = Integer.parseInt(str);
                                } else {
                                    buyCount = inputCount;
                                }
                            }
                            buyCount = inputCount;
                        }
                    }
                    onSureListener.onSuer(buyCount);
                }
                dismiss();
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buy.setCursorVisible(true);
                buy.selectAll();
                Editable editable = buy.getText();
                Selection.setSelection(editable, editable.length());
            }
        });

        minus = (ImageView) view.findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
                                     public void onClick(View v) {
                                         buy.setCursorVisible(false);
                                         buy.selectAll();
                                         String str = buy.getText().toString();
                                         if (str.length() == 0) {
                                             buyCount = defaultCount;
                                         } else {
                                             int inputCount = Integer.parseInt(str);
                                             if (inputCount > maxSize) {
                                                 buyCount = maxSize + stepSize;
                                                 ToastUtil.showToast(getContext(), R.string.buyhint);
                                             } else {
                                                 if (isTen) {
                                                     if (!str.endsWith("0")) {
                                                         str = str.substring(0, str.length() - 1) + "0";
                                                         buyCount = Integer.parseInt(str) + stepSize;
                                                     } else {
                                                         buyCount = inputCount;
                                                     }
                                                 }
                                             }
                                         }
                                         if (buyCount > stepSize)
                                             buyCount = buyCount - stepSize;
                                         buy.setText(String.valueOf(buyCount));
                                     }
                                 }

        );

        add = (ImageView) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
                                   public void onClick(View v) {
                                       buy.setCursorVisible(false);
                                       buy.selectAll();
                                       String str = buy.getText().toString();
                                       if (str.length() == 0) {
                                           buyCount = 0;
                                       } else {
                                           int inputCount = Integer.parseInt(str);
                                           if ((inputCount + stepSize) >= maxSize) {
                                               buyCount = maxSize - stepSize;
                                               ToastUtil.showToast(getContext(), R.string.buyhint);
                                           } else {
                                               if (isTen) {
                                                   buyCount = Integer.parseInt(str.substring(0, str.length() - 1) + "0");
                                               } else {
                                                   buyCount = inputCount;
                                               }
                                           }
                                       }
                                       buyCount = buyCount + stepSize;
                                       buy.setText(String.valueOf(buyCount));
                                   }
                               }

        );
        return view;
    }


    public interface OnSureListener {
        public void onSuer(int buyCount);
    }

}
