/*
 *
 * filters table rows
 * <code>
 * $.simpleTableFilter( $('table'), phrase )
 * </code>
 * arguments:
 *   jQuery object containing table rows
 *   phrase to search for
 *   optional arguments:
 *     column to limit search too (index)
 *     callback - function that accepts the set of elements to be hidden
 */
(function($) {
	$.simpleTableFilter = function(jObject, phrase, column, callback) {
		var hidden = [];
		var displayed = [];
		
		if (this.lastPhrase === phrase){
			return false;
		}

		var matches = function(elem) {
			elem.show();
		};
		
		var getText = "td";

		// if column is defined
		if (column) {			
			if (jObject.find("thead > tr:last > th").length <= column) {
				throw ("given column: " + column + " not found");
			}
			getText = getText + ":eq(" + index + ")";
		}
		

		var query = "tbody:first > tr";
		
		if (phrase.size > this.lastPhrase) {
			if (phrase.slice(0, lastPhrase.size) === phrase){
				// new search phrase is a subset of the last
				// search phrase; only find the items that are
				// currently visible
				query = query + ":visible";
				matches = function() {};
			}
		}
		
		//find the list of potential rows
		var elems = jObject.find(query);
		
		// for each row find the subset of that row which
		// has a matching text
		elems.each( function() {
			var elem = $(this);
			var rowText = elem.find(getText).text();
			if (rowText.indexOf(phrase) > -1) {
				matches(elem);
			} else {
				hidden.push(elem);
			}
		});
		
		lastPhrase = phrase;
				
		if (hidden.length > 0){
			hidden = $(hidden);
			if (callback){
				callback(hidden);
			} else {
				hidden.each(function () {
					this.hide();
				});
			}
		}
		elems = undefined;
		return this;
	};
	
	$.simpleTableFilter.lastPhrase = "";
})(jQuery);
