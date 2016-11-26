package attt.musicteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import attt.musicteam.R;
import attt.musicteam.sharepreference.StateSharePreference;
/**
 * Created by Hue on 11/8/2016.
 */
public class MoreFragment extends Fragment {

    public StateSharePreference statesp;

    public MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, null);
        statesp = new StateSharePreference();
        if (statesp.getState(getActivity()) == StateSharePreference.HISTORY_STATE) {
            getFragmentManager().beginTransaction()
                    .add(R.id.more_container, new MoreHistoryFragment().newInstance()).commit();
        } else
            getFragmentManager().beginTransaction()
                    .add(R.id.more_container, new MoreRootFragment().newInstance()).commit();
        return view;
    }

}
