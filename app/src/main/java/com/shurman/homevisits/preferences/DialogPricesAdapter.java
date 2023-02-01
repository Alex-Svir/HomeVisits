package com.shurman.homevisits.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shurman.homevisits.R;
import com.shurman.homevisits.data.DEntry;

import org.shurman.blindedview.AbsBlindedView;
import org.shurman.blindedview.SingleBlindView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DialogPricesAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final ArrayList<DEntry> mList;

    public DialogPricesAdapter(Context context,ArrayList<DEntry> list) {
        PriceListItem.resetReferences();
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getCount() {
        int sz = mList.size();
        return sz == 0 ? 1 : sz;
    }
    @Override
    public Object getItem(int position) {
        return mList.size() == 0 ? null : mList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return mList.size() == 0 ? -1 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mList.isEmpty()) {
            if (convertView instanceof TextView) {
                return convertView;
            }
            return mInflater.inflate(R.layout.item_dialog_pricelist_placeholder, parent, false);
        }

        if (!(convertView instanceof PriceListItem)) {
            convertView = mInflater.inflate(R.layout.item_dialog_pricelist, parent, false);
        }
        PriceListItem itemView = (PriceListItem) convertView;
        DEntry ps = mList.get(position);
        String s = (ps == null) ? "-//-" : (ps.price + " / " + ps.salary);
        itemView.setup(position, s, ps == null);

        if (ps == null) {
            assert position == mList.size() - 1 : "Fresh added item only allowed at last position";
            itemView.openEditor(this);
        }

        return convertView;
    }

    public boolean add() {
        if (mList.size() > 0 && mList.get(mList.size() - 1) == null)
            return false;
        mList.add(null);
        notifyDataSetChanged();
        return true;
    }

    private void placeItemInOrder(int position) {
        while (position > 0) {
            int d = DEntry.comparator.compare(mList.get(position), mList.get(position - 1));
            if (d > 0) break;
            if (d < 0) {
                DEntry tmp = mList.get(position);
                mList.set(position, mList.get(position - 1));
                mList.set(position - 1, tmp);
                --position;
            } else {
                mList.remove(position--);
            }
        }
        if (position >= mList.size() - 1)
            return;
        while (true) {
            int d = DEntry.comparator.compare(mList.get(position), mList.get(position + 1));
            if (d < 0) break;
            if (d > 0) {
                DEntry tmp = mList.get(position);
                mList.set(position, mList.get(position + 1));
                mList.set(position + 1, tmp);
                ++position;
            } else {
                mList.remove(position);
            }
            if (position >= (mList.size() - 1))
                break;
        }
    }

//--------------------------------------------------------------------------------------------------
    static class PriceListItem extends FrameLayout implements AbsBlindedView.OnInteractionListener {
        private static WeakReference<ListView> sParentList = null;
        private static WeakReference<DialogPricesAdapter> sAdapter = null;

        private static void resetReferences() {
            sParentList = null;
            sAdapter = null;
        }

        private SingleBlindView mBlind;
        private View mEdit;
        private EditText mPrice;
        private EditText mSalary;
        private int position = -1;
        private boolean freshAdded = false;

        public PriceListItem(Context context, AttributeSet attrs) { super(context, attrs); }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            mBlind = findViewById(R.id.blind);
            mEdit = findViewById(R.id.edit);
            mPrice = findViewById(R.id.price);
            mSalary = findViewById(R.id.salary);
            mBlind.setOnInteractionListener(this);
            mSalary.setOnEditorActionListener((v, actionId, event) -> {
                closeEditorIfNeeded(true);
                return true;
            });
        }
        @Override
        public void setActivated(boolean activated) {
            if (isActivated() && !activated) {      //  focus lost
                //closeEditorIfNeeded(false);
                post(() -> closeEditorIfNeeded(false));
            }
            super.setActivated(activated);
        }

        public void setup(int position, CharSequence text, boolean freshAdded) {
            this.position = position;
            mBlind.setText(text);
            this.freshAdded = freshAdded;
            mBlind.setVisibility(View.VISIBLE);
            mEdit.setVisibility(View.GONE);
        }

        @Override
        public void onBlindedItemClick(View view, boolean left) {
            if (left) {
                getParentList().setItemChecked(position, true);
                openEditor();
            } else {
                getParentList().setItemChecked(position, false);
                getAdapter().mList.remove(position);
                getAdapter().notifyDataSetChanged();
            }
        }
        @Override
        public void onBlindClick(View view) { activate(); }
        @Override
        public void onBlindSlideCompleted(View view) { activate(); }

        private void activate() {
            getParentList().setItemChecked(position, true);
        }

        private void openEditor() { openEditor(getAdapter()); }

        private void openEditor(DialogPricesAdapter adapter) {
            mBlind.setVisibility(View.GONE);
            mEdit.setVisibility(View.VISIBLE);
            DEntry value = adapter.mList.get(position);
            if (value != null) {
                mPrice.setText(String.valueOf(value.price));
                mSalary.setText(String.valueOf(value.salary));
            } else {
                mPrice.setText("");
                mSalary.setText("");
            }
        }

        private void closeEditorIfNeeded(boolean save) {
            mBlind.shut();
            if (mEdit.getVisibility() == View.GONE)
                return;
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getWindowToken(), 0);
            mBlind.setVisibility(View.VISIBLE);
            mEdit.setVisibility(View.GONE);
            if (!save) {
                if (freshAdded) { removeFreshAdded(); }
                return;
            }
            try {
                CharSequence priceStr = mPrice.getText();
                int priceVal = Integer.parseInt(priceStr.toString());
                CharSequence salaryStr = mSalary.getText();
                int salaryVal = Integer.parseInt(salaryStr.toString());
                if (0 > salaryVal || salaryVal > priceVal || priceVal > 0xFFFF)
                    throw new NumberFormatException("Illegal range relations");

                DialogPricesAdapter adapter = getAdapter();
                DEntry dEntry = adapter.mList.get(position);
                if (freshAdded) {
                    assert dEntry == null && position == adapter.mList.size() - 1
                            : "Fresh added item must be at last position";
                    adapter.mList.set(position, new DEntry(priceVal, salaryVal, 0));
                    freshAdded = false;
                } else {
                    dEntry.price = priceVal;
                    dEntry.salary = salaryVal;
                }
                mBlind.setText(priceStr + " / " + salaryStr);
                //todo sort / double
                adapter.placeItemInOrder(position);
            } catch (NumberFormatException e) {
                if (freshAdded) { removeFreshAdded(); }
                Toast.makeText(getContext(), R.string.dialog_prefs_pairs_input_err,Toast.LENGTH_SHORT).show();
            }
        }

        private void removeFreshAdded() {
            DialogPricesAdapter adapter = getAdapter();
            assert position == (adapter.mList.size() - 1) && adapter.mList.size() > 0 && adapter.mList.get(position) == null
                    : "Fresh added view must be at the end of list";
            adapter.mList.remove(position);
            adapter.notifyDataSetChanged();
        }

        private ListView getParentList() {
            if (sParentList == null || sParentList.get() == null) {
                ListView lv = null;
                ViewParent vp = getParent();
                while (null != vp) {
                    if (vp instanceof ListView) {
                        lv = (ListView) vp;
                        break;
                    }
                    vp = vp.getParent();
                }
                assert lv != null : "Item must be contained in ListView";
                sParentList = new WeakReference<>(lv);
                return lv;
            }
            return sParentList.get();
        }

        private DialogPricesAdapter getAdapter() {
            if (sAdapter == null || sAdapter.get() == null)
                sAdapter = new WeakReference<>((DialogPricesAdapter)getParentList().getAdapter());
            return sAdapter.get();
        }
    }
}
