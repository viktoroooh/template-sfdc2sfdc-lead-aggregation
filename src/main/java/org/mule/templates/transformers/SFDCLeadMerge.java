/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * The object of this class will take two lists as input and create a third one that will be the merge of the previous two. The identity of an element of the list is defined by its email.
 */
public class SFDCLeadMerge {

	/**
	 * The method will merge the leads from the two lists creating a new one.
	 * 
	 * @param leadsFromOrgA
	 *            leads from organization A
	 * @param leadsFromOrgB
	 *            leads from organization B
	 * @return a list with the merged content of the to input lists
	 */
	public List<Map<String, String>> mergeList(List<Map<String, String>> leadsFromOrgA, List<Map<String, String>> leadsFromOrgB) {
		List<Map<String, String>> mergedLeadsList = new ArrayList<Map<String, String>>();

		// Put all leads from A in the merged mergedleadsList
		for (Map<String, String> leadFromA : leadsFromOrgA) {
			Map<String, String> mergedlead = createMergedLead(leadFromA);
			mergedlead.put("IDInA", leadFromA.get("Id"));
			mergedLeadsList.add(mergedlead);
		}

		// Add the new leads from B and update the exiting ones
		for (Map<String, String> leadsFromB : leadsFromOrgB) {
			Map<String, String> leadFromA = findLeadInList(leadsFromB.get("Email"), mergedLeadsList);
			if (leadFromA != null) {
				leadFromA.put("IDInB", leadsFromB.get("Id"));
			}
			else {
				Map<String, String> mergedLead = createMergedLead(leadsFromB);
				mergedLead.put("IDInB", leadsFromB.get("Id"));
				mergedLeadsList.add(mergedLead);
			}
		}
		return mergedLeadsList;
	}

	private Map<String, String> createMergedLead(Map<String, String> lead) {
		Map<String, String> mergedLead = new HashMap<String, String>();
		mergedLead.put("Name", lead.get("Name"));
		mergedLead.put("Email", lead.get("Email"));
		mergedLead.put("IDInA", "");
		mergedLead.put("IDInB", "");
		return mergedLead;
	}

	private Map<String, String> findLeadInList(String leadEmail, List<Map<String, String>> orgList) {
		if(StringUtils.isBlank(leadEmail))
			return null;
		for (Map<String, String> lead : orgList) {
			if (StringUtils.isNotBlank(lead.get("Email")) && lead.get("Email").equals(leadEmail)) {
				return lead;
			}
		}
		return null;
	}
}
