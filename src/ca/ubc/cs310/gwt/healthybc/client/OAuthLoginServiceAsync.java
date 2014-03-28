/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OAuthLoginServiceAsync
{
    public void     getAuthorizationUrl(Credential credential, AsyncCallback<String> callback);
    public void 	verifySocialUser(Credential credential, AsyncCallback<SocialUser> callback);
    public void     getAccessToken(String sessionId, AsyncCallback<String> callback);
    public void  	logout(String sessionId, int authProvider, AsyncCallback<Void> callback);
}
