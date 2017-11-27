package com.amr.sinnerschraderparsingtask.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.amr.sinnerschraderparsingtask.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.tv_output)
    TextView tvOutput;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new MainPresenter(this, this);
        mPresenter.start();
    }

    @OnClick(R.id.btn_parse)
    void clickParse() {
        mPresenter.validateInput(etInput.getText().toString());
    }


    private ProgressDialog progressDialog;

    @Override
    public void showLoadingIndicator(boolean active) {
        if (active) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void showErrorInput() {
        etInput.setError(getString(R.string.must_input));
    }

    @Override
    public void showResultSuccess(String result) {
        tvOutput.setText(result);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }
}
