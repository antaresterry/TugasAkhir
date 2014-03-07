package com.skripsi.atma.chat;

import com.skripsi.atma.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class ChatOffline extends Activity {

	private ViewGroup messagesContainer;
	private ScrollView scrollContainer;
	private Button sendButton;
	private String output;
	private int i;
	private String inputWord;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_chat_offline);
	    sendButton = (Button) findViewById(R.id.sendButton);
        messagesContainer = (ViewGroup) findViewById(R.id.messagesContainer);
        scrollContainer = (ScrollView) findViewById(R.id.scrollContainer);
        final TextView outputView = new TextView(ChatOffline.this);
        outputView.setTextColor(Color.BLACK);
		outputView.setText("Selamat datang di chat offline! Disini anda bisa menanyakan pertanyaan seputar Unika Atma Jaya. Silahkan memasukkan pertanyaan anda pada textbox di bawah ini.");
        int bgRes2 = R.drawable.right_message_bg;
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params2.gravity = Gravity.RIGHT;
        outputView.setLayoutParams(params2);

        outputView.setBackgroundResource(bgRes2);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesContainer.addView(outputView);

                // Scroll to bottom
                if (scrollContainer.getChildAt(0) != null) {
                    scrollContainer.scrollTo(scrollContainer.getScrollX(), scrollContainer.getChildAt(0).getHeight());
                }
                scrollContainer.fullScroll(View.FOCUS_DOWN);
            }
        });
	    sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendButton();
            }
        });
	}
	
	public void sendButton() {
		final TextView textView = new TextView(ChatOffline.this);
		EditText message = (EditText) findViewById(R.id.messageEdit);
        textView.setTextColor(Color.BLACK);
        textView.setText(message.getText().toString());
        inputWord = message.getText().toString();
        message.setText(null);

        int bgRes = R.drawable.left_message_bg;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        textView.setLayoutParams(params);

        textView.setBackgroundResource(bgRes);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesContainer.addView(textView);

                // Scroll to bottom
                if (scrollContainer.getChildAt(0) != null) {
                    scrollContainer.scrollTo(scrollContainer.getScrollX(), scrollContainer.getChildAt(0).getHeight());
                }
                scrollContainer.fullScroll(View.FOCUS_DOWN);
            }
        });

        checkInput();
		final TextView outputView = new TextView(ChatOffline.this);
        outputView.setTextColor(Color.BLACK);
		outputView.setText(output);
        int bgRes2 = R.drawable.right_message_bg;
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params2.gravity = Gravity.RIGHT;
        outputView.setLayoutParams(params2);

        outputView.setBackgroundResource(bgRes2);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesContainer.addView(outputView);

                // Scroll to bottom
                if (scrollContainer.getChildAt(0) != null) {
                    scrollContainer.scrollTo(scrollContainer.getScrollX(), scrollContainer.getChildAt(0).getHeight());
                }
                scrollContainer.fullScroll(View.FOCUS_DOWN);
            }
        });
	}
	public void checkInput()
	{	
		String[] array = getResources().getStringArray(R.array.Words);
		String checkWord="";
		
		//Checks words
	    for ( i = 0; i < array.length; i ++ ) {    	
			checkWord=array[i];
	    	// Make everything lowercase just for ease...
	        inputWord = inputWord.toLowerCase();	        
	        if (inputWord.contains(checkWord))
	        {
	        	//checking if found
	        	System.out.println("FOUND");
	        	//display the result
	        	switch(i){
	        	case 0 :{
	        		output = "Universitas mempunyai visi menjadi perguruan tinggi terkemuka yang memiliki keunggulan akademik dan professional di tingkat nasional dan internasional yang secara konsisten mewujudkan perpaduan antara iman kristiani, ilmu pengetahuan dan teknologi, serta budaya Indonesia dalam upaya mencerdaskan kehidupan bangsa.";
	        		break;
	        	}
	        	case 1 :{
	        		output = "Universitas mengemban misi:Menyelenggarakan pendidikan akademik dan profesi untuk pengembangan ilmu, profesionalisme, dan karakter peserta didik. Menyelenggarakan penelitian dasar dan terapan untuk kemajuan ilmu pengetahuan, teknologi, dan seni budaya (IPTEKS). Mendarmabaktikan keahlian dalam bidang IPTEKS untuk kepentingan masyarakat. Mengelola pendidikan tinggi secara efektif dan efisien dalam suasana akademik yang beretika dan bermartabat.";
	        		break;
	        	}
	        	case 2 :{
	        		output = "Uskup Agung Jakarta";
	        		break;
	        	}
	        	case 3 :{
	        		output = "Mgr. Ignatius Suharyo";
	        		break;
	        	}
	        	case 4 :{
	        		output = "Drs. Bambang Ismawan, M.S. , Ir. A. Djokowiyono, MSc , Ir. Robby T. Wibawa, Prof. Dr. Maria Farida Indrati, S.H., M.H.";
	        		break;
	        	}
	        	case 5 :{
	        		output = "Vincentius Winarto, Ph.D.";
	        		break;
	        	}
	        	case 6 :{
	        		output = "Bruder Drs. Heribertus Sumarjo, FIC, M.M.";
	        		break;
	        	}
	        	case 7 :{
	        		output = "Linus M. Setiadi, S.E., Ak., M.M., M.B.A.";
	        		break;
	        	}
	        	case 8 :{
	        		output = "Avanti Fontana, Ph.D., CF, CC, dr. Wasista Budiwaluyo, M.H.A., Putri Ira C. Dompas, S.H., M.H., Ir.Aswin Wirjadi, Muljawan Margadana, S.Psi., Stefanus Ginting, S.E., Ir. George The Tjiong Gie, Levi Lana, S.H., L.L.M.";
	        		break;
	        	}
	        	case 9 :{
	        		output = "Ir.Danny Walla";
	        		break;
	        	}
	        	case 10 :{
	        		output = "Rafael K. Abdisa, Dr. Frans Hendra Winarta, S.H., M.H., FCBArb., ACIArb.";
	        		break;
	        	}
	        	case 11 :{
	        		output = "Swandi Budiman, S.E., Akt.";
	        		break;
	        	}
	        	case 12:
	        	case 13:
	        	case 14 :{
	        		output = "dr. Maria Marcia I. Marimba, M.M.";
	        		break;
	        	}
	        	case 15:
	        	case 16 :{
	        		output = "1 Juni 1960";
	        		break;
	        	}
	        	case 17 :{
	        		output = "Ir. J.P. Cho, Ir. Lo Siang Hien-Ginting, Drs. Goei Tjong Tik, I.J. Kasimo, J.B. Legiman, S.H., Drs. F.X. Seda, Pang Lay Kim, Tan Bian Seng, Anton M. Moeliono, St. Munadjat Danusaputro, J.E. Tan, Ben Mang-Reng Say yang tergabung dalam Yayasan Atma Jaya.";
	        		break;
	        	}
	        	case 18 :{
	        		output = "Prof. Dr. Ir. M.M. Lanny W. Pandjaitan, M.T.";
	        		break;
	        	}
	        	case 19 :{
	        		output = "Wakil Rektor 1: Dr.rer.pol. A. Y. Agung Nugroho, Wakil Rektor 2: Dr. Sofian Soegioko, M.M., Wakil Rektor 3: Ir. M. Makdin Sinaga, M.Sc., Wakil Rektor 4: Lina Salim, S.E., M.B.A., MA.,Ph.D., CPM(A)";
	        		break;
	        	}
	        	case 20:
	        	case 21 :{
	        		output = "Rm. B. Hardijantan Dermawan, Pr";
	        		break;
	        	}
	        	case 22:
	        	case 23 :{
	        		output = "Dr.phil. Hana Rochani G. Panggabean, Psi.";
	        		break;
	        	}
	        	case 24:
	        	case 25:
	        	case 26 :{
	        		output = "Andre J. Ata Ujan, M.A., Ph.D";
	        		break;
	        	}
	        	case 27:
	        	case 28:
	        	case 29 :{
	        		output = "Dr. A. Prasetyantoko";
	        		break;
	        	}
	        	case 30:
	        	case 31:
	        	case 32:
	        	case 33:
	        	case 34 :{
	        		output = "Maraden Marcellinus Marcellino, Ph.D";
	        		break;
	        	}
	        	case 35:
	        	case 36:
	        	case 37 :{
	        		output = "Prof. Ir. Hadi Sutanto, MMAE., Ph.D.";
	        		break;
	        	}
	        	case 38:
	        	case 39:
	        	case 40 :{
	        		output = "Tommy Hendra Purwaka, S.H., LL.M., Ph. D.";
	        		break;
	        	}
	        	case 41:
	        	case 42:
	        	case 43 :{
	        		output = "Dr. dr. Soegianto Ali, M.Med.Sc.";
	        		break;
	        	}
	        	case 44:
	        	case 45:
	        	case 46 :{
	        		output = "Dr.phil. Juliana Murniati, M. Si.";
	        		break;
	        	}
	        	case 47:
	        	case 48:
	        	case 49:
	        	case 50 :{
	        		output = "Dr. Diana Elizabeth Waturangi, M. Si";
	        		break;
	        	}
	        	case 51:
	        	case 52:
	        	case 53:
	        	case 54:
	        	case 55:
	        	case 56:
	        	case 57:
	        	case 58 :{
	        		output = "dr. Binawati Hadikusuma, M.S.";
	        		break;
	        	}
	        	case 59:
	        	case 60:
	        	case 61:
	        	case 62:
	        	case 63:
	        	case 64:
	        	case 65:
	        	case 66 :{
	        		output = "Pintor P. Siahaan, S.E.";
	        		break;
	        	}
	        	case 67:
	        	case 68:
	        	case 69:
	        	case 70:
	        	case 71:
	        	case 72:
	        	case 73:
	        	case 74:
	        	case 75:
	        	case 76:
	        	case 77:
	        	case 78 :{
	        		output = "Drs. FL. Widie Kastyanto, M.M.";
	        		break;
	        	}
	        	case 79:
	        	case 80:
	        	case 81:
	        	case 82:
	        	case 83:
	        	case 84:
	        	case 85:
	        	case 86:
	        	case 87:
	        	case 88:
	        	case 89:
	        	case 90:
	        	case 91 :{
	        		output = "Ir. M. Makdin Sinaga, M.Sc.";
	        		break;
	        	}
	        	case 92:
	        	case 93:
	        	case 94:
	        	case 95:
	        	case 96:
	        	case 97:
	        	case 98:
	        	case 99:
	        	case 100:
	        	case 101:
	        	case 102:
	        	case 103:
	        	case 104 :{
	        		output = "Ir. Anthon De Fretes, M.Sc.";
	        		break;
	        	}
	        	case 105:
	        	case 106:
	        	case 107:
	        	case 108:
	        	case 109:
	        	case 110:
	        	case 111:
	        	case 112:
	        	case 113:
	        	case 114:
	        	case 115:
	        	case 116:
	        	case 117 :{
	        		output = "Dr. Clara R. P. Ajisuksmo, M. Sc.";
	        		break;
	        	}
	        	case 118:
	        	case 119:
	        	case 120:
	        	case 121:
	        	case 122 :{
	        		output = "Margaretha Sri Udari, S.Sos., M.Hum.";
	        		break;
	        	}
	        	case 123:
	        	case 124:
	        	case 125:
	        	case 126:
	        	case 127 :{
	        		output = "Diao Ai Lien, Ph.D.";
	        		break;
	        	}
	        	case 128:
	        	case 129:
	        	case 130:
	        	case 131:
	        	case 132:
	        	case 133:
	        	case 134:
	        	case 135:
	        	case 136:
	        	case 137:
	        	case 138:
	        	case 139 :{
	        		output = "Drs. Kasdin Sihotang, M.Hum";
	        		break;
	        	}
	        	case 140:
	        	case 141:
	        	case 142:
	        	case 143:
	        	case 144:
	        	case 145:
	        	case 146:
	        	case 147 :{
	        		output = "Drs. Kasdin Sihotang, M.Hum";
	        		break;
	        	}
	        	case 148:
	        	case 149:
	        	case 150:
	        	case 151:
	        	case 152:
	        	case 153:
	        	case 154:
	        	case 155:
	        	case 156:
	        	case 157:
	        	case 158:
	        	case 159:
	        	case 160 :{
	        		output = "Ir. Linda Wijayanti, M.Sc";
	        		break;
	        	}
	        	case 161:
	        	case 162:
	        	case 163:
	        	case 164:
	        	case 165:
	        	case 166:
	        	case 167:
	        	case 168:
	        	case 169:
	        	case 170:
	        	case 171:
	        	case 172:
	        	case 173 :{
	        		output = "Danny Natalies, S.T., M.M.";
	        		break;
	        	}
	        	case 174:
	        	case 175:
	        	case 176:
	        	case 177:
	        	case 178:
	        	case 179:
	        	case 180:
	        	case 181:
	        	case 182:
	        	case 183:
	        	case 184:
	        	case 185 :{
	        		output = "Nugroho Adipradana, SH., M.Sc";
	        		break;
	        	}
	        	case 186:
	        	case 187:
	        	case 188:
	        	case 189:
	        	case 190:
	        	case 191:
	        	case 192:
	        	case 193 :{
	        		output = "Faizah Sari, Ph. D.";
	        		break;
	        	}
	        	case 194:
	        	case 195:
	        	case 196:
	        	case 197:
	        	case 198:
	        	case 199:
	        	case 200:
	        	case 201:
	        	case 202:
	        	case 203:
	        	case 204:
	        	case 205:
	        	case 206:
	        	case 207:
	        	case 208:	
	        	case 209 :{
	        		output = "Paulina Chandrasari Kusuma, M. Hum.";
	        		break;
	        	}
	        	case 210:
	        	case 211:	
	        	case 212 :{
	        		output = "Jl. Jenderal Sudirman 51, Jakarta 12930";
	        		break;
	        	}
	        	case 213:
	        	case 214:
	        	case 215:
	        	case 216:
	        	case 217:
	        	case 218:
	        	case 219:
	        	case 220 :{
	        		output = "(62-21) 5727615, 5703306, 5708823";
	        		break;
	        	}
	        	case 221:
	        	case 222 :{
	        		output = "(62-21) 5747912";
	        		break;
	        	}
	        	case 223:
	        	case 224:
	        	case 225 :{
	        		output = "Fakultas Ekonomi, Fakultas Hukum, Fakultas Psikologi, Fakultas Ilmu Administrasi Bisnis dan Komunikasi, Fakultas Teknik, Fakultas Kedokteran, Fakultas Teknobiologi, Fakultas Keguruan dan Ilmu Pendidikan.";
	        		break;
	        	}  
	        	}
	        	break;
	        	}
	        else{
        		output = "Tolong masukkan kembali pertanyaan Anda. Bila pertanyaan anda tidak terjawab, anda bisa menggunakan fasilitas chat online atau menghubungi pihak Atma Jaya pada nomor telepon (62-21) 5727615, 5703306, dan 5708823. Anda juga dapat melihat akun facebook Unika Atma Jaya di http://www.facebook.com/unika.jayafull, dan akun twitter Unika Atma Jaya @UnikaAtmaJaya";
	        }
	    }
	}
}
