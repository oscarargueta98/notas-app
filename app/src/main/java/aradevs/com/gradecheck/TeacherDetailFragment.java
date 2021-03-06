package aradevs.com.gradecheck;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import aradevs.com.gradecheck.adapters.AdapterTeacherSubjects;
import aradevs.com.gradecheck.helpers.ClipboardHelper;
import aradevs.com.gradecheck.helpers.ImagesHelper;
import aradevs.com.gradecheck.helpers.ServerHelper;
import aradevs.com.gradecheck.helpers.SharedHelper;
import aradevs.com.gradecheck.models.Teachers;
import aradevs.com.gradecheck.models.Users;
import aradevs.com.gradecheck.singleton.AppSingleton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherDetailFragment extends Fragment {

    private static Teachers t = new Teachers();
    @BindView(R.id.teacherdetailCopy)
    TextView teacherdetailCopy;
    private Context context;
    @BindView(R.id.teacherdetailPb)
    ProgressBar teacherdetailPb;
    @BindView(R.id.teacherdetailName)
    TextView teacherdetailName;
    @BindView(R.id.teacherdetailImage)
    CircleImageView teacherdetailImage;
    @BindView(R.id.teacherdetailEmail)
    TextView teacherdetailEmail;
    @BindView(R.id.teacherdetailRecyclerView)
    RecyclerView teacherdetailRecyclerView;
    Unbinder unbinder;
    SharedHelper sh;
    Users u;

    public TeacherDetailFragment() {

    }

    //request grades data method
    private void requestData(final View view, String id) {
        JsonObjectRequest request = new JsonObjectRequest(
                ServerHelper.URL + ServerHelper.TEACHERS_ROOT + id + ServerHelper.ALL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        teacherdetailPb.setVisibility(View.GONE);
                        teacherdetailRecyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
                        teacherdetailRecyclerView.setLayoutManager(mLayoutManager);
                        RecyclerView.Adapter mAdapter = new AdapterTeacherSubjects(response);
                        teacherdetailRecyclerView.setAdapter(mAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error.networkResponse.statusCode == 401) {
                                sh.sessionExpired(getActivity());
                            } else {
                                Toast.makeText(context, new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Error de servidor", Toast.LENGTH_SHORT).show();
                        }
                        teacherdetailPb.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", u.getToken());
                return params;
            }
        };
        //send request to queue
        AppSingleton.getInstance(context).addToRequestQueue(request, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        t = bundle.getParcelable("teacher");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String fullName = t.getUsers().getName() + " " + t.getUsers().getSurname();

        teacherdetailName.setText(fullName);
        teacherdetailEmail.setText(t.getUsers().getEmail());
        sh = new SharedHelper(getActivity());
        u = sh.getUser();

        ImagesHelper.setImage(ServerHelper.URL + ServerHelper.PROFILE_IMAGE + t.getUsers().getId() + "/withToken/" + u.getToken(),
                teacherdetailImage,
                getActivity().getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        // Required empty public constructor
        context = getActivity().getApplicationContext();
        try {
            requestData(getView(), t.getId());
        } catch (Exception e) {
            Log.e("Skipped request data", "Probably cleaning fragment");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }
    @OnClick(R.id.teacherdetailCopy)
    public void onViewClicked() {

        ClipboardHelper ch = new ClipboardHelper();
        ch.copyToClipBoard(getActivity(), "Email", teacherdetailEmail.getText());
    }
}
