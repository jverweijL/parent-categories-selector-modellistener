package com.liferay.demo.modellistener;



import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

/**
 * @author jverweij
 */
@Component(
	immediate = true,
	property = {
		// TODO enter required service properties
	},
	service = ModelListener.class
)
public class ParentCategoriesSelectorModellistener
		extends BaseModelListener<AssetEntry> {

	@Override
	public void onAfterCreate(AssetEntry asset) throws ModelListenerException {
		setAncestorCategories(asset);
	}

	@Override
	public void onAfterUpdate(AssetEntry asset) throws ModelListenerException {
		setAncestorCategories(asset);
	}

	private void setAncestorCategories(AssetEntry asset) {
		List<AssetCategory> categories = asset.getCategories();
		for (AssetCategory category:categories) {
			try {
				List<AssetCategory> ancestors = category.getAncestors();
				for (AssetCategory ancestor:ancestors) {
					//TODO check vocabularyID from config
					_AssetCategoryLocalService.addAssetEntryAssetCategory(asset.getEntryId(),ancestor.getCategoryId());
				}
			} catch (PortalException e) {
				e.printStackTrace();
			}
		}
	}

	@Reference
	private AssetCategoryLocalService _AssetCategoryLocalService;

	private static final Log _log = LogFactoryUtil.getLog(ParentCategoriesSelectorModellistener.class);

}