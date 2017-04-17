package com.hongbaogou.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hongbaogou.MainActivity;
import com.hongbaogou.R;
import com.hongbaogou.activity.GoodsDetailActivity;
import com.hongbaogou.activity.LoginWindowActivity;
import com.hongbaogou.activity.PayOrderActivity;
import com.hongbaogou.adapter.ShoppingCarAdapter;
import com.hongbaogou.bean.BaseListBean;
import com.hongbaogou.bean.BaseObjectBean;
import com.hongbaogou.bean.ShoppingCartBean;
import com.hongbaogou.dialog.DeleteGoodsDialog;
import com.hongbaogou.dialog.DeleteGoodsDialog.onSureDeleteListener;
import com.hongbaogou.dialog.UpdateBuyCountDiolag;
import com.hongbaogou.global.ConstantValues;
import com.hongbaogou.listener.OnShoppingCartDeleteListener;
import com.hongbaogou.listener.OnShoppingCartListListener;
import com.hongbaogou.listener.UpdateListCountListener;
import com.hongbaogou.request.ShoppingCartDeleteRequest;
import com.hongbaogou.request.ShoppingCartListRequest;
import com.hongbaogou.request.UpdateListCountRequest;
import com.hongbaogou.utils.Pref_Utils;
import com.hongbaogou.utils.ToastUtil;
import com.hongbaogou.view.NetErrorView;
import com.hongbaogou.view.refresh.PtrClassicFrameLayout;
import com.hongbaogou.view.refresh.PtrFrameLayout;
import com.hongbaogou.view.refresh.PtrHandler;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, NetErrorView.OnReloadListener, OnShoppingCartListListener, ShoppingCarAdapter.OnStateChangelistener, OnShoppingCartDeleteListener, UpdateBuyCountDiolag.OnSureListener, onSureDeleteListener, UpdateListCountListener {

    private View rootView;
    private PtrClassicFrameLayout ptrFrameLayout;
    private ListView shoppingCarList;
    private ShoppingCarAdapter shoppingCarAdapter;
    private LinearLayout emptyView;
    private NetErrorView netErrorView;
    private TextView editTextView;
    //底部选择的数量及总价格
    private TextView total;
    //结算对象
    private TextView balance;
    //全选按钮
    private CheckBox checkbox;
    //删除按钮
    private TextView delete;
    private LinearLayout selectCount;
    private RelativeLayout bottomView;
    private Resources resources;
    private boolean isEditable = false;
    private ShoppingCartListRequest shoppingCartListRequest;
    private ShoppingCartDeleteRequest shoppingCartDeleteRequest;
    private UpdateListCountRequest updateListCountRequest;
    private boolean isFresh = false;
    //删除时选择商品的数量
    private TextView count;
    private Button toFistPage;

    private int deleteCount = 0;
    private LocalBroadcastManager localBroadcastManager;
    private RefrshReceiver refrshReceiver;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        initView();

        shoppingCartListRequest = new ShoppingCartListRequest();
        shoppingCartDeleteRequest = new ShoppingCartDeleteRequest();
        updateListCountRequest = new UpdateListCountRequest();

        return rootView;
    }


    private void initView() {
        resources = getResources();

        netErrorView = (NetErrorView) rootView.findViewById(R.id.netErrorView);
        netErrorView.setOnReloadListener(this);

        total = (TextView) rootView.findViewById(R.id.total);
        balance = (TextView) rootView.findViewById(R.id.balance);
        balance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (YYJXApplication.isLogin) {
                if (Pref_Utils.getBoolean(getActivity(),"isLogin")) {
                    Intent intent = new Intent(getActivity(), PayOrderActivity.class);
                    intent.putExtra("info", shoppingCarAdapter.getGoods());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginWindowActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.down_up, R.anim.staticanim);
                }
            }
        });
        checkbox = (CheckBox) rootView.findViewById(R.id.checkbox);
        checkbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shoppingCarAdapter.selectAction(checkbox.isChecked());
                if (checkbox.isChecked()) {
                    int selectCount = shoppingCarAdapter.getCount();
                    count.setText(resources.getString(R.string.selected_count, selectCount));
                    deleteCount = shoppingCarAdapter.getCount();
                } else {
                    count.setText(resources.getString(R.string.selected_count, 0));
                    deleteCount = 0;
                }
            }
        });
        selectCount = (LinearLayout) rootView.findViewById(R.id.selectCount);
        delete = (TextView) rootView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeleteGoodsDialog deleteGoodsDialog = new DeleteGoodsDialog();
                deleteGoodsDialog.setOnSureDeleteListener(ListFragment.this);
                deleteGoodsDialog.show(getActivity().getSupportFragmentManager(), "delete");
            }
        });
        bottomView = (RelativeLayout) rootView.findViewById(R.id.bottomView);
        count = (TextView) rootView.findViewById(R.id.count);

        editTextView = (TextView) rootView.findViewById(R.id.edit);
        editTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isEditable = !isEditable;
                if (isEditable) {
                    editTextView.setText(resources.getString(R.string.finish));
                    count.setVisibility(View.VISIBLE);
                    count.setText(resources.getString(R.string.selected_count, 0));

                    shoppingCarAdapter.selectAction(false);
                    deleteCount = 0;
                    deleteCountChange(deleteCount);
                } else {
                    editTextView.setText(resources.getString(R.string.edit));
                }

                bottomViewState(isEditable);
                shoppingCarAdapter.setEditState(isEditable);
                shoppingCarAdapter.notifyDataSetChanged();
            }
        });

        toFistPage = (Button) rootView.findViewById(R.id.toFistPage);
        toFistPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });

        emptyView = (LinearLayout) rootView.findViewById(R.id.emptyView);
        shoppingCarList = (ListView) rootView.findViewById(R.id.shoppingCarList);
        shoppingCarList.setEmptyView(emptyView);
        shoppingCarAdapter = new ShoppingCarAdapter(getActivity());
        shoppingCarList.setAdapter(shoppingCarAdapter);
        shoppingCarAdapter.setOnStateChangelistener(this);
        shoppingCarList.setOnItemClickListener(this);
        shoppingCarList.setOnItemLongClickListener(this);

        ptrFrameLayout = (PtrClassicFrameLayout) rootView.findViewById(R.id.refreshView);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header) {
                boolean flag = true;
                if (android.os.Build.VERSION.SDK_INT < 14) {
                    flag = shoppingCarList.getChildCount() > 0 && (shoppingCarList.getFirstVisiblePosition() > 0 || shoppingCarList.getChildAt(0).getTop() < shoppingCarList.getPaddingTop());
                } else {
                    flag = shoppingCarList.canScrollVertically(-1);
                }
                return !flag;
            }

            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    public void run() {
                        isFresh = true;
                        String uid = Pref_Utils.getString(getActivity(), "uid");
                        shoppingCartListRequest.shoppingCartListRequest(uid, ListFragment.this);
                    }
                }, 10);
            }
        });


        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantValues.REFRESH);
        refrshReceiver = new RefrshReceiver();
        localBroadcastManager.registerReceiver(refrshReceiver, intentFilter);
    }

    class RefrshReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (ConstantValues.REFRESH.equals(intent.getAction())) {
                    if(shoppingCartListRequest!=null){
                        String uid = Pref_Utils.getString(getActivity(), "uid");
                        shoppingCartListRequest.shoppingCartListRequest(uid, ListFragment.this);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        isAttach = false;
        localBroadcastManager.unregisterReceiver(refrshReceiver);
        super.onDestroy();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("TAG", "=====onItemClick================ " + position);
        if (isEditable) {
            ShoppingCartBean shoppingCartBean = shoppingCarAdapter.getItemBean(position);
            boolean isSelected = shoppingCartBean.isSelected();
            deleteCount = isSelected ? deleteCount - 1 : deleteCount + 1;
            deleteCountChange(deleteCount);
            count.setText(resources.getString(R.string.selected_count, deleteCount));

            shoppingCartBean.setIsSelected(!isSelected);
            shoppingCarAdapter.notifyDataSetChanged();
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), GoodsDetailActivity.class);
            ShoppingCartBean shoppingCartBean = shoppingCarAdapter.getItemBean(position);
            intent.putExtra("id", shoppingCartBean.getShopid());
            intent.putExtra("issue", shoppingCartBean.getQishu());
            startActivity(intent);
        }
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("TAG", "=====onItemLongClick================ " + position);
        if (!isEditable) {
            isEditable = true;
            editTextView.setText(resources.getString(R.string.finish));
            count.setVisibility(View.VISIBLE);
            count.setText(resources.getString(R.string.selected_count, 1));
            deleteCount = 1;
            deleteCountChange(deleteCount);

            bottomViewState(isEditable);
            shoppingCarAdapter.setEditState(isEditable);
            ShoppingCartBean shoppingCartBean = shoppingCarAdapter.getItemBean(position);
            shoppingCartBean.setIsSelected(true);
            shoppingCarAdapter.notifyDataSetChanged();
        }
        return true;
    }

    /*
     * 底部视图对象的切换
     */
    private void bottomViewState(boolean flag) {
        if (flag) {
            total.setVisibility(View.GONE);
            balance.setVisibility(View.GONE);
            checkbox.setVisibility(View.VISIBLE);
            selectCount.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        } else {
            total.setVisibility(View.VISIBLE);
            balance.setVisibility(View.VISIBLE);
            checkbox.setVisibility(View.GONE);
            selectCount.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
    }

    /*
     * 清空购物车
     */
    public void clearCart() {
        if (shoppingCarAdapter != null) {
            shoppingCarAdapter.clearData();
            bottomView.setVisibility(View.GONE);
        }
    }

    public boolean initRequest() {

//        ptrFrameLayout.autoRefresh(true);

        String uid = Pref_Utils.getString(getActivity(), "uid");
        shoppingCartListRequest.shoppingCartListRequest(uid, this);
        shoppingCarAdapter.notifyDataSetChanged();
        return false;
    }

    private boolean isAttach = true;
    public void onShoppingCartListSuccess(BaseListBean baseListBean) {
        if(!isAttach){
            return;
        }


        ptrFrameLayout.refreshComplete();
        if (baseListBean.getStatus() == 1) {
            netErrorView.loadSuccess();
            ptrFrameLayout.setVisibility(View.VISIBLE);
            List<ShoppingCartBean> shoppingCartBeans = baseListBean.getData();
            if (shoppingCartBeans.size() != 0) {
                netErrorView.setVisibility(View.GONE);
                editTextView.setVisibility(View.VISIBLE);
                bottomView.setVisibility(View.VISIBLE);
                if (isEditable) {
                    deleteCount = 0;
                    count.setText(resources.getString(R.string.selected_count, 0));
                    checkbox.setChecked(false);
                } else {

                }
                shoppingCarAdapter.reloadData(shoppingCartBeans);

                updatePrice();

                Intent intent = new Intent();
                intent.setAction("messageStateChange");
                intent.putExtra("count", shoppingCarAdapter.getCount());
                getActivity().sendBroadcast(intent);
            } else {
                List<ShoppingCartBean> data = new ArrayList<>();
                shoppingCarAdapter.reloadData(data);
                updatePrice();
                Intent intent = new Intent();
                intent.setAction("messageStateChange");
                intent.putExtra("count", shoppingCarAdapter.getCount());
                getActivity().sendBroadcast(intent);
            }
        } else {
            netErrorView.loadFail();
        }
        isFresh = false;
    }

    public void onShoppingCartListFailed(VolleyError error) {
        netErrorView.loadFail();
        isFresh = false;
    }

    public void onReload() {
        String uid = Pref_Utils.getString(getActivity(), "uid");
        shoppingCartListRequest.shoppingCartListRequest(uid, this);
    }

    public void deleteCountChange(int deleteCount) {
        if (deleteCount == 0) {
            checkbox.setChecked(false);
        } else {
            checkbox.setChecked(true);
        }
        count.setText(resources.getString(R.string.selected_count, deleteCount));
    }

    public void priceChange(String id, String shopid, int num) {

        updateListCountRequest.updateListCountRequest(id, shopid, num, this);
        updatePrice();
    }

    private void updatePrice() {
        String price = String.valueOf(shoppingCarAdapter.getTotalPrice());
        String str = resources.getString(R.string.shopping_total, shoppingCarAdapter.getCount(), price);
        SpannableStringBuilder priceBuilder = new SpannableStringBuilder(str);
        ForegroundColorSpan color = new ForegroundColorSpan(getResources().getColor(R.color.list_total_color));
        priceBuilder.setSpan(color, str.length() - 3 - price.length(), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        total.setText(priceBuilder);
    }

    public void onShoppingCartDeleteSuccess(BaseObjectBean baseObjectBean) {
        if (baseObjectBean.getStatus() == 1) {
            shoppingCarAdapter.updateList();
            isEditable = false;
            checkbox.setChecked(false);
            editTextView.setText(resources.getString(R.string.edit));
            bottomViewState(false);
            shoppingCarAdapter.setEditState(false);
            int count = shoppingCarAdapter.getCount();
            if (count == 0) {
                bottomView.setVisibility(View.GONE);
                editTextView.setVisibility(View.GONE);
            } else {
                updatePrice();
            }
            Intent intent = new Intent();
            intent.setAction("messageStateChange");
            intent.putExtra("count", shoppingCarAdapter.getCount());
            getActivity().sendBroadcast(intent);
            Toast.makeText(getActivity(), R.string.delete_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.delete_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void onShoppingCartDeleteFailed(VolleyError error) {
        Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
    }


    private ShoppingCartBean shoppingCartBean;
    private TextView buyCountView;

    public void updateBuyCount(TextView buyCountView, int position) {
        this.buyCountView = buyCountView;
        shoppingCartBean = shoppingCarAdapter.getItemBean(position);
        int remainder = Integer.parseInt(shoppingCartBean.getZongrenshu()) - Integer.parseInt(shoppingCartBean.getCanyurenshu());
        int isTen = shoppingCartBean.getIs_ten();
        boolean flag = isTen == 1 ? true : false;
        int stepSize = isTen == 1 ? 10 : 1;

        UpdateBuyCountDiolag updateBuyCountDiolag = new UpdateBuyCountDiolag();
        updateBuyCountDiolag.setOnSureListener(this);
        updateBuyCountDiolag.setMaxSize(remainder);
        updateBuyCountDiolag.setDefaultCount(shoppingCartBean.getGonumber());
        updateBuyCountDiolag.setStepSize(stepSize);
        updateBuyCountDiolag.setIsTen(flag);
        updateBuyCountDiolag.show(getActivity().getSupportFragmentManager(), "update");
    }

    /*
     * 确认修改
     */
    public void onSuer(int buyCount) {
        shoppingCartBean.setGonumber(buyCount);
        buyCountView.setText(String.valueOf(buyCount));
        updatePrice();

        updateListCountRequest.updateListCountRequest(shoppingCartBean.getId(), shoppingCartBean.getShopid(), buyCount, this);

    }

    /*
     * 确认删除
     */
    public void onSureDelete() {
        String ids = shoppingCarAdapter.getDeleteIds();
        if (ids.trim().length() == 0) {
            ToastUtil.showToast(getContext().getApplicationContext(), R.string.delete_id);
        } else {
            String uid = Pref_Utils.getString(getActivity(), "uid");
            shoppingCartDeleteRequest.ShoppingCartDeleteRequest(uid, ids, ListFragment.this);
        }
    }


    public void onUpdateListCountSuccess(BaseObjectBean baseObjectBean) {

    }

    public void onUpdateListCountFailed(VolleyError error) {

    }
}
