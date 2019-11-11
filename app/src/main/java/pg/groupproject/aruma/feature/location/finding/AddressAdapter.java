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

import com.google.android.gms.common.util.Strings;

import java.util.ArrayList;
import java.util.List;

import pg.groupproject.aruma.R;

class AddressAdapter extends BaseAdapter implements Filterable {

    private static final int RESOURCE_ID = R.layout.listview_adress_element;
    private static final String NO_VALUE = "-";
    private final Context context;
    private final Object lock = new Object();
    private List<NominatimLocation> values = new ArrayList<>();
    private int previousValuesCount = 0;

    AddressAdapter(@NonNull Context context) {
        super();
        this.context = context;
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

    public void update(List<NominatimLocation> addresses) {
        values.clear();
        values.addAll(addresses);
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

    private String getNullSafeValue(String value) {
        return Strings.isEmptyOrWhitespace(value) ? NO_VALUE : value;
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
}
