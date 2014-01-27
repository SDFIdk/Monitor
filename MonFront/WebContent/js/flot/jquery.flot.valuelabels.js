/**
 * Value Labels Plugin for flot.
 * http://leonardoeloy.github.com/flot-valuelabels
 * http://wiki.github.com/leonardoeloy/flot-valuelabels/
 *
 * Using canvas.fillText instead of divs, which is better for printing - by Leonardo Eloy, March 2010.
 * Tested with Flot 0.6 and JQuery 1.3.2.
 *
 * Original homepage: http://sites.google.com/site/petrsstuff/projects/flotvallab
 * Released under the MIT license by Petr Blahos, December 2009.
 */
(function ($) {
    var options = {
        valueLabels: {
	    show: false,
        showAsHtml: false, // Set to true if you wanna switch back to DIV usage (you need plot.css for this)
        showLastValue: false, // Use this to show the label only for the last value in the series
        labelFormatter: function(v) {return v} // Format the label value to what you want
        }
    };
    
    function init(plot) {
        plot.hooks.draw.push(function (plot, ctx) {
	    if (!plot.getOptions().valueLabels.show) return;
            
            var showLastValue = plot.getOptions().valueLabels.showLastValue;
            var showAsHtml = plot.getOptions().valueLabels.showAsHtml;
            var labelFormatter = plot.getOptions().valueLabels.labelFormatter;
            var ctx = plot.getCanvas().getContext("2d");
	    $.each(plot.getData(), function(ii, series) {
                    // Workaround, since Flot doesn't set this value anymore
                    series.seriesIndex = ii;
		    if (showAsHtml) plot.getPlaceholder().find("#valueLabels"+ii).remove();
		    var html = '<div id="valueLabels' + series.seriesIndex + '" class="valueLabels">';
                    
		    var last_val = null;
		    var last_x = -1000;
		    var last_y = -1000;
		    for (var i = 0; i < series.data.length; ++i) {
			if (series.data[i] == null || (showLastValue && i != series.data.length-1))  continue;

			var x = series.data[i][0], y = series.data[i][1];
			if (x < series.xaxis.min || x > series.xaxis.max || y < series.yaxis.min || y > series.yaxis.max)  continue;
			var val = y;
			var valnum = parseFloat(y);

			if (series.valueLabelFunc) 
                            val = series.valueLabelFunc({ series: series, seriesIndex: ii, index: i });
			//val = Math.round(val*10)/10; // Nikolaj: Added this line to ensure val is holding only one decimal 
                        val = ""+val;
                        val = labelFormatter(val);

			if (val!=last_val || i==series.data.length-1) {
				var xx = series.xaxis.p2c(x)+plot.getPlotOffset().left;
				var yy = series.yaxis.p2c(y)-12+plot.getPlotOffset().top;
				if (Math.abs(yy-last_y)>20 || last_x<xx) {
					last_val = val;
					last_x = xx + val.length*8;
					last_y = yy;
                                        
                                        if (!showAsHtml) {
                                            // Little 5 px padding here helps the number to get
                                            // closer to points
					    /*
					    This section is added by Nikolaj Kamstrup to center the label on top/below the bars
					    Start of section
					    */
                                            if (Math.abs(valnum)<10) {
						if(valnum<0)
						    x_pos = xx-6;
						else
						x_pos = xx+3; // Nikolaj: Added logic to center the label
                                            }
                                            else if (Math.abs(valnum)<100) {
						if(valnum<0)
						    x_pos = xx-10;
						else
						    x_pos = xx-6;
                                            }
                                            else if (Math.abs(valnum)<1000) {
						if(valnum<0)
						    x_pos = xx-15; // There is a minus prepended to the number
						else
						    x_pos = xx-10;
                                            }
                                            else if (Math.abs(valnum)<10000) {
						if(valnum<0)
						    x_pos = xx-20;
						else
						    x_pos = xx-15;
                                            }
					    else {
						if(valnum<0)
						    x_pos = xx-26;
						else
						    x_pos = xx-20;
					    }
					    
                                            if(y<0)
                                                y_pos = yy+26; //Nikolaj: Added another 20 px to make sure the label are placed under the bars when y is negative 
                                            else
                                                y_pos = yy+3;


					    /*
					    End of section
					    */

					    
                                            // If the value is on the top of the canvas, we need
                                            // to push it down a little
                                            if (yy <= 0) y_pos = 18;
                                            // The same happens with the x axis
                                            if (xx >= plot.width()) x_pos = plot.width();

                                            ctx.font = "500 12px verdana"; // Nikolaj: added font as built in font is too small
					    ctx.fillStyle = "#555555"; // Nikolaj: added this to get a grey font color
					    ctx.fillText(val, x_pos, y_pos);                
                                        } else {
                                            var head = '<div style="left:' + xx + 'px;top:' + yy + 'px;" class="valueLabel';
                                            var tail = '">' + val + '</div>';
                                            html+= head + "Light" + tail + head + tail;
                                        }
				}
			}
		    }
                    
                    if (showAsHtml) {
                        html+= "</div>";
                        plot.getPlaceholder().append(html);
                    }
		});
        });
    }
    
    $.plot.plugins.push({
        init: init,
        options: options,
        name: 'valueLabels',
        version: '1.1'
    });
})(jQuery);

