package com.example.pomodoro.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pomodoro.R;
import com.example.pomodoro.viewModel.MainViewModel;
import com.example.pomodoro.viewModel.Project;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// TODO: 1. Add project in recyclerView

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "ItemFragment";

    private MainViewModel mModel;                 // ViewModel
    private ProjectRecyclerViewAdapter mAdapter;  // Adapter of recyclerView for projects

    // TODO:(NoActionNeeded) Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO:(NoActionNeeded) Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    // TODO:(NoActionNeeded) Customize parameter initialization
    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int columnCount) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        // Get the ViewModel.
        mModel = new ViewModelProvider(requireActivity(),
                new SavedStateViewModelFactory(requireActivity().getApplication(),this))
                .get(MainViewModel.class);


        // create adapter for RecyclerView
        mAdapter = new ProjectRecyclerViewAdapter(mModel);


        // Create the observer which updates the UI.
        final Observer<List<Project>> observer = new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> projectList) {
                mAdapter.setProjectList(projectList);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mModel.getPrjListLive().observe(this, observer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final List<Project> list;

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 添加项目按钮
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "You clicked add project", Toast.LENGTH_SHORT).show();
            }
        });

        setHasOptionsMenu(true);

        // 构造RecycleView
        RecyclerView recyclerView = view.findViewById(R.id.lvPrjList);
        // Set the layoutManager
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        // Set the adapter
        recyclerView.setAdapter(mAdapter);

        // 创建ItemTouchHelper.Callback，实现回调方法
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            // 返回允许滑动的方向
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // 返回可滑动方向，通过使用一个int，在各个bit位标记来记录。
                // 这里drag支持上下方向，swipe支持左右方向。
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                // 返回设置了标识位的复合int
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            // 允许drag的前提下，当ItemTouchHelper想要将拖动的项目从其旧位置移动到新位置时调用
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // 获取被拖拽item和目标item的适配器索引（适配器索引是该item对应数据集的索引，getLayoutPosition是当前布局的位置）
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                // 交换数据集的数据
                mAdapter.swapItem(from, to);

                // 通知Adapter更新，此动作应是Adapter的内生逻辑
                //adapter.notifyItemMoved(from, to);

                // 返回true表示item移到了目标位置
                return true;
            }

            // 允许swipe的前提下，当用户滑动ViewHolder触发临界时调用
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 获取滑动的item对应的适配器索引
                int pos = viewHolder.getAdapterPosition();
                // 从数据集移除数据
                mAdapter.removeItem(pos);

                // 通知Adapter更新，此动作应是Adapter的内生逻辑
                //adapter.notifyItemRemoved(pos);
            }

            //当用户操作完毕后，记录项目清单的顺序
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                mAdapter.adjustPriority();
            }
        };
        // 传入ItemTouchHelper.Callback
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        // 将touchHelper和recyclerView绑定
        touchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // 为ActionBar扩展菜单项
        inflater.inflate(R.menu.actionbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_create_data:
                Toast.makeText(getContext(), "You clicked settings", Toast.LENGTH_SHORT).show();
                /**
                 * {@link RecyclerView.Adapter} Create dummy project list that can display a {@link DummyItem}.
                 * TODO: Clean it in future and Replace the implementation with code for your data type.
                 */
                mModel.createDummyPrjList();
                mModel.createDummyActList();
                return true;

            case R.id.action_delete_data:
                // Clear database for testing purpose
                // TODO: Clean in future
                mModel.deleteAllProjects();
                mModel.deleteAllActivities();
                return true;

            case R.id.action_about:
                Toast.makeText(getContext(), "You clicked about", Toast.LENGTH_SHORT).show();

                return NavigationUI.onNavDestinationSelected(item,
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment))
                        || super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}