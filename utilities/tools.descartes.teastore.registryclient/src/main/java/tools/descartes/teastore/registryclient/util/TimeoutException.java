/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.descartes.teastore.registryclient.util;


/**
 * Exception thrown if a 504 response was received.
 * @author Joakim von Kistowski
 *
 */
public class TimeoutException extends RuntimeException {


	private static final long serialVersionUID = -7025097849696056898L;
	/**
	 * The corresponding HTTP error code.
	 */
	public static final int ERROR_CODE = 504;
	

	/**
	 * Creates a new NotFoundException.
	 */
	public TimeoutException() {
		super();
	}
	
}
