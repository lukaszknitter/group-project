package pg.groupproject.aruma.feature.location.finding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import pg.groupproject.aruma.R;

public class AddressAdapter extends BaseAdapter implements Filterable {

    private static final int RESOURCE_ID = R.layout.listview_adress_element;
    private static final String NO_VALUE = "-";
    private final Context context;
    private List<NominatimLocation> values = new ArrayList<>();
    private Supplier<FragmentActivity> supplierMainActivity;

    public AddressAdapter(@NonNull Context context, Supplier<FragmentActivity> supplierMainActivity) {
        this.context = context;
        this.supplierMainActivity = supplierMainActivity;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public NominatimLocation getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
        }
        NominatimLocation address = getItem(position);
        if (address == null) {
            return convertView;
        }

        TextView name = convertView.findViewById(R.id.listview_elem_name);
        name.setText(getNullSafeValue(NominatimLocationFormatting.formatFeatureName(address.getDisplayName())));
        TextView country = convertView.findViewById(R.id.listview_elem_address);
        country.setText(getNullSafeValue(NominatimLocationFormatting.formatAddress(address.getAddress())));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                filterResults.count = getCount();
                filterResults.values = values;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    private String getNullSafeValue(String value) {
        return Strings.isEmptyOrWhitespace(value) ? NO_VALUE : value;
    }

    public void update(List<NominatimLocation> addresses) {
        if (!addresses.isEmpty()) {
            supplierMainActivity.get().runOnUiThread(() -> {
                values.clear();
                values.addAll(addresses);
                this.notifyDataSetChanged();
            });
        }
    }
}
