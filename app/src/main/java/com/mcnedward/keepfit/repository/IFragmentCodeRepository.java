package com.mcnedward.keepfit.repository;

import com.mcnedward.keepfit.activity.fragment.BaseFragment;
import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.model.Goal;

import java.util.List;

/**
 * Created by Edward on 2/23/2016.
 */
public interface IFragmentCodeRepository extends IRepository<FragmentCode> {

    List<FragmentCode> getFragmentCodesSorted();

}
